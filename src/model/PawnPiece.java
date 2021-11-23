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
       /* int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        if (direction == 1 && !(newX < getLocationOnX() - 1 || newX > getLocationOnX() + 1)) {
            if (newY == getLocationOnY() + direction)
                return true;
            if (newY == getLocationOnY() + 2 * direction && firstMove) {
                firstMove = false;
                return true;
            }
        } else if (!(newX < getLocationOnX() - 1 || newX > getLocationOnX() + 1)){
            if (newY == getLocationOnY() + direction)
                return true;
            if (newY == getLocationOnY() + 2 * direction && firstMove) {
                firstMove = false;
                return true;
            }
        }
        return false;*/
        return true;
    }

    @Override
    public boolean validMoveTo(int newX, int newY, ArrayList<Piece> pieces) {
        //TODO: check whether the pawn is allowed to move to the specified position
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
