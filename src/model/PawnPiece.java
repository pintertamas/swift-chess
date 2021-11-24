package model;

import utils.PieceColor;
import utils.Functions;

import javax.swing.*;
import java.util.ArrayList;

public class PawnPiece extends Piece {
    boolean firstMove;

    public PawnPiece(PieceColor color, int location) {
        super(color, location);
        this.firstMove = true;
    }

    @Override
    public boolean canMoveTo(int newX, int newY) {

        if (newX > 8 || newX < 0 || newY > 8 || newY < 0)
            return false;

        if (getColor() == PieceColor.WHITE) {
            // we can move upwards
            if (newX <= getCurrentLocation().x + 1 && newX >= getCurrentLocation().x - 1) {
                if (newY == getCurrentLocation().y - 1) {
                    return true;
                }
                if (newY == getCurrentLocation().y - 2 && firstMove && newX == getCurrentLocation().x) {
                    firstMove = false;
                    return true;
                }
            }
        }

        if (getColor() == PieceColor.BLACK) {
            // we can move downwards
            if (newX <= getCurrentLocation().x + 1 && newX >= getCurrentLocation().x - 1) {
                if (newY == getCurrentLocation().y + 1) {
                    return true;
                }
                if (newY == getCurrentLocation().y + 2 && firstMove && newX == getCurrentLocation().x) {
                    firstMove = false;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean freeToMove(int newX, int newY, ArrayList<Piece> pieces) {
        return false;
    }

    @Override
    public boolean wontLoseTheGame(int newX, int newY, ArrayList<Piece> pieces) {
        return false;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("pawn_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("pawn_black.png");
    }
}
