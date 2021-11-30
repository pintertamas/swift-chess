package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;

public class BishopPiece extends Piece {
    public BishopPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point from) {
        boolean[][] movingPoints = new boolean[8][8];
        checkDiagonalMoves(from, movingPoints, 1, 1);
        checkDiagonalMoves(from, movingPoints, -1, 1);
        checkDiagonalMoves(from, movingPoints, 1, -1);
        checkDiagonalMoves(from, movingPoints, -1, -1);

        //getBoard().printBoard(movingPoints);

        return movingPoints;
    }

    private void checkDiagonalMoves(Point from, boolean[][] moves, int relX, int relY) {
        int offset = 0;
        while (true) {
            // relX&relY from {-1;+1}
            int newX = from.x + offset * relX;
            int newY = from.y + offset * relY;
            PieceColor enemyColor = this.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            if (!this.isFreeFromColorAndValid(newX, newY, enemyColor) && !Functions.isOutside(newX, newY)) {
                System.out.println(newX + " " + newY);
                moves[newX - 1][newY - 1] = true;
                return;
            }
            if (this.isFreeFromColorAndValid(newX, newY, getColor())) {
                System.out.println(newX + " " + newY);
                moves[newX - 1][newY - 1] = true;
                offset++;
            } else {
                return;
            }
        }
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("bishop_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("bishop_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
}
