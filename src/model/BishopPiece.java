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
    public boolean[][] getMoves(Point include, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];

        checkDiagonalMoves(include, exclude, movingPoints, 1, 1);
        checkDiagonalMoves(include, exclude, movingPoints, -1, 1);
        checkDiagonalMoves(include, exclude, movingPoints, 1, -1);
        checkDiagonalMoves(include, exclude, movingPoints, -1, -1);

        return movingPoints;
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
