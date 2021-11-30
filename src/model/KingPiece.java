package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class KingPiece extends Piece {
    public KingPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point from) {
        boolean[][] movingPoints = new boolean[8][8];
        if (this.freeToMoveTo(from.x - 1, from.y)) movingPoints[from.x - 1 - 1][from.y - 1] = true;
        if (this.freeToMoveTo(from.x - 1, from.y - 1)) movingPoints[from.x - 1 - 1][from.y - 1 - 1] = true;
        if (this.freeToMoveTo(from.x - 1, from.y + 1)) movingPoints[from.x - 1 - 1][from.y + 1 - 1] = true;
        if (this.freeToMoveTo(from.x, from.y - 1)) movingPoints[from.x - 1][from.y - 1 - 1] = true;
        if (this.freeToMoveTo(from.x, from.y + 1)) movingPoints[from.x - 1][from.y + 1 - 1] = true;
        if (this.freeToMoveTo(from.x + 1, from.y)) movingPoints[from.x + 1 - 1][from.y - 1] = true;
        if (this.freeToMoveTo(from.x + 1, from.y - 1)) movingPoints[from.x + 1 - 1][from.y - 1 - 1] = true;
        if (this.freeToMoveTo(from.x + 1, from.y + 1)) movingPoints[from.x + 1 - 1][from.y + 1 - 1] = true;
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
