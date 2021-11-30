package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BishopPiece extends Piece {
    public BishopPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
    }

    @Override
    public boolean[][] getMoves(Point from) {
        boolean[][] movingPoints = new boolean[8][8];
        int offset = 0;
        while (true) {
            int newX = from.x - offset;
            int newY = from.y - offset;
            if (this.freeToMoveTo(newX, newY)) {
                movingPoints[newX - 1][newY - 1] = true;
                System.out.println("newX: " + newX + " newY: " + newY);
                offset++;
            } else {
                break;
            }
        }
        offset = 0;
        while (true) {
            int newX = from.x + offset;
            int newY = from.y - offset;
            if (this.freeToMoveTo(newX, newY)) {
                movingPoints[newX - 1][newY - 1] = true;
                System.out.println("newX: " + newX + " newY: " + newY);
                offset++;
            } else {
                break;
            }
        }
        offset = 0;
        while (true) {
            int newX = from.x - offset;
            int newY = from.y + offset;
            if (this.freeToMoveTo(newX, newY)) {
                movingPoints[newX - 1][newY - 1] = true;
                System.out.println("newX: " + newX + " newY: " + newY);
                offset++;
            } else {
                break;
            }
        }
        offset = 0;
        while (true) {
            int newX = from.x + offset;
            int newY = from.y + offset;
            if (this.freeToMoveTo(newX, newY)) {
                movingPoints[newX - 1][newY - 1] = true;
                System.out.println("newX: " + newX + " newY: " + newY);
                offset++;
            } else {
                break;
            }
        }

        return movingPoints;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("bishop_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("bishop_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
}
