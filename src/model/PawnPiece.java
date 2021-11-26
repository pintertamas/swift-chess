package model;

import utils.PieceColor;
import utils.Functions;

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
    public boolean[][] getMoves(ArrayList<Piece> pieces) {
        /*if (Functions.isOutside(newX, newY))
            return false;
        if (newX <= getCurrentLocation().x + 1 && newX >= getCurrentLocation().x - 1) {
            int direction = getColor() == PieceColor.WHITE ? 1 : -1;

            if ((newY == getCurrentLocation().y - direction)
                    || (newY == getCurrentLocation().y - 2 * direction && firstMove && newX == getCurrentLocation().x)) {
                firstMove = false;
                return true;
            }

            if ((newY == getCurrentLocation().y + direction)
                    || (newY == getCurrentLocation().y + 2 * direction && firstMove && newX == getCurrentLocation().x)) {
                firstMove = false;
                return true;
            }
        }
        return false;*/
        boolean[][] movingPoints = new boolean[8][8];
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        int newY = currentLocation.y - 2 * direction;
        if (firstMove && newY <= 8 && newY > 0) {
            movingPoints[currentLocation.x][newY] = true;
        }
        newY = currentLocation.y - direction;
        if (newY <= 8 && newY > 0) {
            movingPoints[currentLocation.x][newY] = true;
        }

        return movingPoints;
    }

    @Override
    public boolean freeToMove(int newX, int newY, ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getColor().equals(this.getColor())) {
                if (piece.getCurrentLocation().equals(new Point(newX, newY))) {
                    return false;
                }
            } else if (piece.getCurrentLocation().equals(new Point(newX, newY)) && newX == currentLocation.x) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean[][] addHittingMovesTo(Point newLocation, boolean[][] moves) {
        /*
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        int newX = currentLocation.x - 1;
        int newY = currentLocation.y - direction;
        if (!Functions.isOutside(newX, newY))
            moves[newX - 1][newY - 1] = true;
        newX = currentLocation.x + 1;
        newY = currentLocation.y - direction;
        if (!Functions.isOutside(newX, newY))
            moves[newX - 1][newY - 1] = true;
        */
        boolean[][] hits = getHittingMovesFrom(newLocation);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (hits[i][j])
                    moves[i][j] = true;
            }
        }

        System.out.println();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(hits[i][j] ? 1 + " " : 0 + " ");
            }
            System.out.println();
        }
        return moves;
    }

    @Override
    public boolean[][] getHittingMovesFrom(Point fromLocation) {
        boolean[][] hittingMoves = new boolean[8][8];
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        int hitX = fromLocation.x - 1;
        int hitY = fromLocation.y - direction;
        if (hitX > 0 && hitY > 0 && hitY <= 8)
            hittingMoves[hitX][hitY] = true;
        hitX = fromLocation.x + 1;
        if (hitX <= 8)
            hittingMoves[hitX][hitY] = true;

        return hittingMoves;
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
    public utils.Piece getType() {
        return utils.Piece.PAWN;
    }
}
