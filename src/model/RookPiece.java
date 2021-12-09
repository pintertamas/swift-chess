package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;

/**
 * A bástyákat reprezentálja
 */
public class RookPiece extends Piece {
    public RookPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point include, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];

        checkLongMoves(include, exclude, movingPoints, 0, 1);
        checkLongMoves(include, exclude, movingPoints, 0, -1);
        checkLongMoves(include, exclude, movingPoints, 1, 0);
        checkLongMoves(include, exclude, movingPoints, -1, 0);

        return movingPoints;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("rook_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("rook_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }
}
