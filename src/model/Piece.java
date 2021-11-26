package model;

import utils.Functions;
import utils.PieceColor;

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

    public abstract boolean[][] getMoves(ArrayList<Piece> pieces);

    public boolean canMoveTo(int newX, int newY, ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            if (getHittingMovesFrom(new Point(newX, newY))[piece.getCurrentLocation().x - 1][piece.getCurrentLocation().y - 1]) {
                return true;
            }
        }
        return getMoves(pieces)[newX][newY];
    }

    public boolean freeToMove(int newX, int newY, ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getColor().equals(this.getColor())) {
                if (piece.getCurrentLocation().equals(new Point(newX, newY))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void handleHits(int newX, int newY, ArrayList<Piece> pieces) {
        for (int i = 0; i < pieces.size(); i++) {
            if (!pieces.get(i).getColor().equals(this.getColor())) {
                if (pieces.get(i).getCurrentLocation().equals(new Point(newX, newY))) {
                    if (pieces.get(i).getType().equals(utils.Piece.KING)) {
                        System.out.println("The king of alliance " + pieces.get(i).getColor() + " has fallen!");
                    }
                    pieces.get(i).removeFrom(pieces);
                }
            }
        }
    }

    public abstract boolean[][] addHittingMovesTo(Point newLocation, boolean[][] moves);

    public abstract boolean[][] getHittingMovesFrom(Point fromLocation);

    public boolean causesChessToSelectedTeam(Point newLocation, ArrayList<Piece> pieces, PieceColor color) {
        boolean[][] dangerZone = new boolean[8][8];

        for (Piece piece : pieces) {
            if (!piece.getColor().equals(color))
                dangerZone = piece.addHittingMovesTo(newLocation, dangerZone);
        }

        Piece king = getKing(pieces);

        if (king == null) return true;

        if (dangerZone[king.getCurrentLocation().x - 1][king.getCurrentLocation().y - 1]) {
            System.out.println("Your king is in danger");
            return true;
        } else return false;
    }

    public boolean possibleToStopChess(Point newLocation, ArrayList<Piece> pieces) {
        boolean[][] dangerZone = new boolean[8][8];

        for (Piece piece : pieces) {
            if (!piece.getColor().equals(this.color)) {
                if (newLocation.equals(piece.getCurrentLocation()))
                    dangerZone = piece.addHittingMovesTo(newLocation, dangerZone);
            }
        }

        Piece king = getKing(pieces);

        if (king == null) return false;

        if (dangerZone[king.getCurrentLocation().x - 1][king.getCurrentLocation().y - 1]) {
            System.out.println("Your king is still in danger");
            return false;
        } else return true;

    }

    private Piece getKing(ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getType() == utils.Piece.KING && piece.getColor() == this.getColor()) {
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

    public abstract utils.Piece getType();
}