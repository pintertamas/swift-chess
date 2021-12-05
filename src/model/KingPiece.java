package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;

/**
 * A királyt reprezentálja
 */
public class KingPiece extends Piece {
    public KingPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point include, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];
        checkMoves(include, exclude, movingPoints, 1, 1);
        checkMoves(include, exclude, movingPoints, 1, 0);
        checkMoves(include, exclude, movingPoints, 1, -1);
        checkMoves(include, exclude, movingPoints, 0, 1);
        checkMoves(include, exclude, movingPoints, 0, -1);
        checkMoves(include, exclude, movingPoints, -1, 1);
        checkMoves(include, exclude, movingPoints, -1, 0);
        checkMoves(include, exclude, movingPoints, -1, -1);

        //getBoard().printBoard(movingPoints);
        return movingPoints;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("king_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("king_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }
}
