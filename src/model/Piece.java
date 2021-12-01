package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece extends JLabel implements Serializable {

    protected final PieceColor color;
    protected final int boardLocation;
    protected Point lastLocation;
    protected Point currentLocation;
    protected final boolean[][] moves;
    protected final ChessBoard board;

    public Piece(PieceColor color, int location, ChessBoard board) {
        // it centers the image
        super(Functions.getImage("blank.png"));
        this.color = color;
        this.boardLocation = location;
        moves = new boolean[8][8];
        this.board = board;
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

    public abstract boolean[][] getMoves(Point from, Point exclude);

    public boolean canMoveTo(int newX, int newY, Point exclude) {
        if (Functions.isOutside(newX, newY))
            return false;
        return getMoves(getCurrentLocation(), exclude)[newX - 1][newY - 1];
    }

    public boolean isFreeFromColorAndValid(int newX, int newY, PieceColor color, Point exclude) {
        if (Functions.isOutside(newX, newY))
            return false;
        for (Piece piece : getBoard().getPieces()) {
            if (new Point(newX, newY).equals(exclude))
                return true;
            if ((piece.getCurrentLocation().equals(new Point(newX, newY)))
                    && piece.getColor().equals(color))
                return false;
        }
        return true;
    }

    public void checkDiagonalMoves(Point exclude, Point from, boolean[][] moves, int relX, int relY) {
        int offset = 1;
        while (true) {
            // relX&relY from {-1;+1}
            int newX = from.x + offset * relX;
            int newY = from.y + offset * relY;
            PieceColor enemyColor = this.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            if (!this.isFreeFromColorAndValid(newX, newY, enemyColor, exclude) && !Functions.isOutside(newX, newY)) {
                moves[newX - 1][newY - 1] = true;
                return;
            }
            if (this.isFreeFromColorAndValid(newX, newY, getColor(), exclude)) {
                moves[newX - 1][newY - 1] = true;
                offset++;
            } else {
                return;
            }
        }
    }

    public void placementUpdate(int newX, int newY, ArrayList<Piece> pieces) {
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

    public boolean[][] addMovesTo(Point exclude, boolean[][] moves) {
        boolean[][] possibleMoves = getMoves(getCurrentLocation(), exclude);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possibleMoves[i][j]) {
                    moves[j][i] = true;
                }
            }
        }
        return moves;
    }

    public boolean selectedTeamIsInChess(Point exclude, PieceColor color) {
        boolean[][] dangerZone = new boolean[8][8];

        for (Piece piece : getBoard().getPieces()) {
            if (!piece.getColor().equals(color))
                dangerZone = piece.addMovesTo(exclude, dangerZone);
        }

        Piece king = getKing(color);

        if (king == null) return true;

        if (dangerZone[king.getCurrentLocation().y - 1][king.getCurrentLocation().x - 1]) {
            System.out.println("Your king is in danger");
            return true;
        } else return false;
    }

    private Piece getKing(PieceColor color) {
        for (Piece piece : getBoard().getPieces()) {
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
            pieces.remove(this);
            this.setVisible(false);
            this.getParent().remove(this);
        }
    }

    public abstract PieceType getType();

    public ChessBoard getBoard() {
        return board;
    }
}