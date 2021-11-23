package model;

import utils.Functions;
import utils.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece extends JLabel implements Serializable, Cloneable {

    protected PieceColor color;
    protected int boardLocation;
    protected Point lastLocation;

    public Piece(PieceColor color, int location) {
        super(Functions.getImage("pawn_black.png"));
        this.color = color;
        this.boardLocation = location;
    }

    public int getBoardLocation() {
        return boardLocation;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
        System.out.println("lastlocation: " + lastLocation);
    }

    public Point getLastLocation() {
        return lastLocation;
    }

    public PieceColor getColor() {
        return this.color;
    }

    public ImageIcon getImage() {
        return color == PieceColor.WHITE ? getWhiteImage() : getBlackImage();
    }

    public abstract boolean canMoveTo(int newX, int newY);

    public abstract boolean validMoveTo(int newX, int newY, ArrayList<Piece> pieces);

    public int getLocationOnX() {
        return boardLocation - (boardLocation / 8) * 8 + 1;
    }

    public int getLocationOnY() {
        return boardLocation / 8 + 1;
    }

    public abstract ImageIcon getWhiteImage();

    public abstract ImageIcon getBlackImage();
}