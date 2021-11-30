package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;

public class KnightPiece extends Piece {
    public KnightPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point from, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];

        checkMoves(from, exclude, movingPoints, 2, 1);
        checkMoves(from, exclude, movingPoints, 1, 2);
        checkMoves(from, exclude, movingPoints, -2, 1);
        checkMoves(from, exclude, movingPoints, -1, 2);

        return movingPoints;
    }

    private void checkMoves(Point from, Point exclude, boolean[][] moves, int relX, int relY) {
        int newX = from.x + relX;
        int newY = from.y + relY;
        if (this.isFreeFromColorAndValid(newX, newY, this.getColor(), exclude) && !Functions.isOutside(newX, newY)) moves[newX - 1][newY - 1] = true;
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
