package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QueenPiece extends Piece {
    public QueenPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point from, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];

        checkDiagonalMoves(from, exclude, movingPoints, 1, 1);
        checkDiagonalMoves(from, exclude, movingPoints, -1, 1);
        checkDiagonalMoves(from, exclude, movingPoints, 1, -1);
        checkDiagonalMoves(from, exclude, movingPoints, -1, -1);
        checkDiagonalMoves(from, exclude, movingPoints, 0, 1);
        checkDiagonalMoves(from, exclude, movingPoints, 0, -1);
        checkDiagonalMoves(from, exclude, movingPoints, 1, 0);
        checkDiagonalMoves(from, exclude, movingPoints, -1, 0);

        return movingPoints;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("queen_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("queen_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }
}
