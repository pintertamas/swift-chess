package model;

import utils.Functions;
import utils.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece extends JLabel implements Serializable {

    protected PieceColor color;
    protected int boardLocation;
    protected Point lastLocation;
    protected Point currentLocation;

    public Piece(PieceColor color, int location) {
        // it centers the image
        super(Functions.getImage("blank.png"));
        this.color = color;
        this.boardLocation = location;
    }

    public int getBoardLocation() {
        return boardLocation;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
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

    public abstract boolean freeToMove(int newX, int newY, ArrayList<Piece> pieces);

    public abstract boolean wontLoseTheGame(int newX, int newY, ArrayList<Piece> pieces);

    public int getXLocationFromComponentNumber() {
        return boardLocation - (boardLocation / 8) * 8 + 1;
    }

    public int getYLocationFromComponentNumber() {
        return boardLocation / 8 + 1;
    }

    public void setCurrentLocation(int posX, int posY) {
        currentLocation = new Point(posX, posY);
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public abstract ImageIcon getWhiteImage();

    public abstract ImageIcon getBlackImage();
}