package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;

/**
 * A lovakat reprezentálja
 */
public class KnightPiece extends Piece {
    public KnightPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point include, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];

        checkMoves(include, exclude, movingPoints, 2, 1);
        checkMoves(include, exclude, movingPoints, 2, -1);
        checkMoves(include, exclude, movingPoints, 1, 2);
        checkMoves(include, exclude, movingPoints, 1, -2);
        checkMoves(include, exclude, movingPoints, -2, 1);
        checkMoves(include, exclude, movingPoints, -2, -1);
        checkMoves(include, exclude, movingPoints, -1, 2);
        checkMoves(include, exclude, movingPoints, -1, -2);

        return movingPoints;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("knight_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("knight_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}
