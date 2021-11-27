package model;

import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece extends JLabel implements Serializable {

    protected PieceColor color;
    protected int boardLocation;
    protected Point lastLocation;
    protected Point currentLocation;
    protected boolean[][] moves;

    public Piece(PieceColor color, int location) {
        // it centers the image
        super(Functions.getImage("blank.png"));
        this.color = color;
        this.boardLocation = location;
        moves = new boolean[8][8];
    }

    public int getBoardLocation() {
        return boardLocation;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Point getLastLocation() {
        return lastLocation;
    }

    public PieceColor getColor() {
        return this.color;
    }

    public ImageIcon getImage() {
        return color == PieceColor.WHITE ? getWhiteImage() : getBlackImage();
    }

    public abstract boolean[][] getMoves(Point from, ArrayList<Piece> pieces);

    public boolean canMoveTo(int newX, int newY, ArrayList<Piece> pieces) {
        return getMoves(currentLocation, pieces)[newX - 1][newY - 1];
    }

    public boolean freeToMoveTo(int newX, int newY, ArrayList<Piece> pieces) {
        if (Functions.isOutside(newX, newY))
            return false;
        for (Piece piece : pieces) {
            if (piece.getCurrentLocation().equals(new Point(newX, newY))
                    && piece.getColor().equals(getColor()))
                return false;
        }
        return true;
    }

    public void move(int newX, int newY, ArrayList<Piece> pieces) {
        // handle hits
        for (int i = 0; i < pieces.size(); i++) {
            if (!pieces.get(i).getColor().equals(this.getColor())) {
                if (pieces.get(i).getCurrentLocation().equals(new Point(newX, newY))) {
                    if (pieces.get(i).getType().equals(PieceType.KING)) {
                        System.out.println("The king of alliance " + pieces.get(i).getColor() + " has fallen!");
                    }
                    pieces.get(i).removeFrom(pieces);
                }
            }
        }
        // move
        this.setCurrentLocation(newX, newY);
        this.setVisible(false);
    }

    public boolean[][] addMovesTo(boolean[][] moves, Point from, ArrayList<Piece> pieces) {
        boolean[][] possibleMoves = getMoves(from, pieces);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possibleMoves[j][i])
                    moves[i][j] = true;
            }
        }
        return moves;
    }

    public boolean selectedTeamIsInChess(Point from, ArrayList<Piece> pieces, PieceColor color) {
        boolean[][] dangerZone = new boolean[8][8];

        for (Piece piece : pieces) {
            if (!piece.getColor().equals(color))
                dangerZone = piece.addMovesTo(dangerZone, from, pieces);
        }

        Piece king = getKing(pieces, color);

        if (king == null) return true;

        if (dangerZone[king.getCurrentLocation().y - 1][king.getCurrentLocation().x - 1]) {
            System.out.println("Your king is in danger");
            return true;
        } else return false;
    }

    private Piece getKing(ArrayList<Piece> pieces, PieceColor color) {
        for (Piece piece : pieces) {
            if (piece.getType() == PieceType.KING && piece.getColor() == color) {
                return piece;
            }
        }
        return null;
    }

    public void init(ArrayList<Piece> pieces) {
        this.setIcon(this.getImage());
        this.setCurrentLocation(new Point(this.getXLocationFromComponentNumber(), this.getYLocationFromComponentNumber()));
        pieces.add(this);
    }

    public int getXLocationFromComponentNumber() {
        return boardLocation - (boardLocation / 8) * 8 + 1;
    }

    public int getYLocationFromComponentNumber() {
        return boardLocation / 8 + 1;
    }

    public void setCurrentLocation(int newX, int newY) {
        currentLocation = new Point(newX, newY);
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public abstract ImageIcon getWhiteImage();

    public abstract ImageIcon getBlackImage();

    public void removeFrom(ArrayList<Piece> pieces) {
        if (pieces.contains(this)) {
            System.out.println("Piece removed: " + this);
            pieces.remove(this);
            this.setVisible(false);
            this.getParent().remove(this);
        } else {
            System.out.println("This piece is not contained by the given list");
        }
    }

    public abstract PieceType getType();
}