package model;

import utils.Functions;
import utils.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QueenPiece extends Piece {
    public QueenPiece(PieceColor color, int location) {
        super(color, location);
    }

    @Override
    public boolean[][] getMoves(ArrayList<Piece> pieces) {
        return new boolean[0][];
    }

    @Override
    public boolean[][] addHittingMovesTo(Point newLocation, boolean[][] moves) {
        return moves;
    }

    @Override
    public boolean[][] getHittingMovesFrom(Point fromLocation) {
        return new boolean[0][];
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
    public utils.Piece getType() {
        return utils.Piece.QUEEN;
    }
}
