package model;

import utils.PieceColor;
import utils.Functions;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PawnPiece extends Piece {
    boolean firstMove;

    public PawnPiece(PieceColor color, int location) {
        super(color, location);
        this.firstMove = true;
    }

    @Override
    public boolean[][] getMoves(Point from, ArrayList<Piece> pieces) {
        boolean[][] movingPoints = new boolean[8][8];
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        int newYDoubleStep = from.y - 2 * direction;
        int newYSingleStep = from.y - direction;
        boolean isBlocked = false;
        if (newYSingleStep <= 8 && newYSingleStep > 0) {
            for (Piece piece : pieces) {
                if (piece.getCurrentLocation().equals(new Point(from.x, newYSingleStep))) {
                    isBlocked = true;
                    break;
                }
            }
            movingPoints[from.x - 1][newYSingleStep - 1] = !isBlocked;
        }
        if (firstMove && newYDoubleStep <= 8 && newYDoubleStep > 0) {
            for (Piece piece : pieces) {
                if (piece.getCurrentLocation().equals(new Point(from.x, newYDoubleStep))) {
                    isBlocked = true;
                    break;
                }
            }
            movingPoints[from.x - 1][newYDoubleStep - 1] = !isBlocked;
        }
        int newX = from.x - 1;
        handleHittingMoves(pieces, movingPoints, newYSingleStep, newX);
        newX = from.x + 1;
        handleHittingMoves(pieces, movingPoints, newYSingleStep, newX);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(movingPoints[j][i] ? "1 " : "0 ");
            }
            System.out.println();
        }

        return movingPoints;
    }

    @Override
    public boolean[][] addMovesTo(boolean[][] moves, Point from, ArrayList<Piece> pieces) {
        boolean[][] hittingMoves = super.addMovesTo(moves, from, pieces);
        System.out.println();
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;

        if (!Functions.isOutside(from.y - direction))
            hittingMoves[from.y - direction - 1][from.x - 1] = false;

        if (!Functions.isOutside(from.y - 2 * direction))
            hittingMoves[from.y - 2 * direction - 1][from.x - 1] = false;


        return hittingMoves;
    }

    private void handleHittingMoves(ArrayList<Piece> pieces, boolean[][] movingPoints, int newYSingleStep, int newX) {
        if (!Functions.isOutside(newX, newYSingleStep)) {
            //for (Piece piece : pieces) {
                //if (piece.getCurrentLocation().equals(new Point(newX, newYSingleStep)) && piece.getColor() != getColor() || true) {
                    movingPoints[newX - 1][newYSingleStep - 1] = true;
                    //break;
                //}
            //}
        }
    }

    @Override
    public void move(int newX, int newY, ArrayList<Piece> pieces) {
        super.move(newX, newY, pieces);
        this.firstMove = false;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("pawn_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("pawn_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}
