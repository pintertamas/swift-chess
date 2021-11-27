package model;

import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QueenPiece extends Piece {
    public QueenPiece(PieceColor color, int location) {
        super(color, location);
    }

    @Override
    public boolean[][] getMoves(Point from, ArrayList<Piece> pieces) {
        return new boolean[8][8];
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
