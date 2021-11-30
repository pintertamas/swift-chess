package controller;

import model.*;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

import utils.*;

import javax.swing.*;

public class ChessBoard extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    Piece chessPiece;
    int xAdjustment;
    int yAdjustment;
    ArrayList<Piece> pieces;
    boolean whiteTurn;
    boolean whiteChess;
    boolean blackChess;

    public ChessBoard() {
        whiteTurn = true;
        Dimension boardSize = new Dimension(640, 640);
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
        pieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel(new BorderLayout());
                chessBoard.add(square);
                Color lightColor = new Color(234, 216, 186);
                Color darkColor = new Color(174, 133, 106);
                if (i % 2 == 0) square.setBackground(j % 2 == 0 ? lightColor : darkColor);
                else square.setBackground(j % 2 == 0 ? darkColor : lightColor);
            }
        }

        JPanel panel = (JPanel) chessBoard.getComponent(0);
        addWhitePieces();
        addBlackPieces();
        drawPieces();
    }

    private void addWhitePieces() {
        addPawns(PieceColor.WHITE, 48);
        addRoyalFamily(PieceColor.WHITE, 59, 60);
        addBishops(PieceColor.WHITE, 58, 61);
    }

    private void addBlackPieces() {
        addPawns(PieceColor.BLACK, 8);
        addRoyalFamily(PieceColor.BLACK, 3, 4);
        addBishops(PieceColor.BLACK, 2, 5);
    }


    private void drawPieces() {
        for (Piece piece : pieces) {
            JPanel panel = (JPanel) chessBoard.getComponent(piece.getBoardLocation());
            panel.add(piece);
        }
    }

    private void addPawns(PieceColor color, int from) {
        Piece piece;
        for (int i = from; i < from + 8; i++) {
            piece = new PawnPiece(color, i, this);
            piece.init(pieces);
        }
    }

    private void addRoyalFamily(PieceColor color, int kingLocation, int queenLocation) {
        Piece king = new KingPiece(color, kingLocation, this);
        Piece queen = new QueenPiece(color, queenLocation, this);
        king.init(pieces);
        queen.init(pieces);
    }

    private void addBishops(PieceColor color, int firstBishopLocation, int secondBishopLocation) {
        Piece firstBishop = new BishopPiece(color, firstBishopLocation, this);
        Piece secondBishop = new BishopPiece(color, secondBishopLocation, this);
        firstBishop.init(pieces);
        secondBishop.init(pieces);
    }

    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());

        if (c.getClass().equals(JPanel.class))
            return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (Piece) c;
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setLastLocation(chessPiece.getLocation());
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }

    public void mouseDragged(MouseEvent e) {
        if (chessPiece == null) return;
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    }

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;
        int newX = Functions.getLocationOnX(e.getX(), chessBoard.getSize().width);
        int newY = Functions.getLocationOnY(e.getY(), chessBoard.getSize().height);
        Component component;

        if ((isYourTurn() && chessPiece.canMoveTo(newX, newY))) {
            PieceColor enemyColor = chessPiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            if ((wasNotInChess() &&
                    (!chessPiece.selectedTeamIsInChess(new Point(newX, newY), chessPiece.getColor())
                            || (!chessPiece.selectedTeamIsInChess(new Point(newX, newY), enemyColor))))) {
                checkChess(newX, newY);
                chessPiece.placementUpdate(newX, newY, pieces);
                component = chessBoard.findComponentAt(e.getX(), e.getY());
                whiteTurn = !whiteTurn;
            } else if (!wasNotInChess() && chessPiece.selectedTeamIsInChess(new Point(newX, newY), chessPiece.getColor())) {
                revokeChess();
                chessPiece.placementUpdate(newX, newY, pieces);
                component = chessBoard.findComponentAt(e.getX(), e.getY());
                whiteTurn = !whiteTurn;
            } else {
                component = cannotMove();
            }
        } else {
            component = cannotMove();
        }
        Container parent = (Container) component;
        parent.add(chessPiece);
        chessPiece.setVisible(true);
        checkChess(chessPiece.getCurrentLocation().x, chessPiece.getCurrentLocation().y);
    }

    private boolean isYourTurn() {
        return chessPiece.getColor() == PieceColor.WHITE && whiteTurn || (chessPiece.getColor() == PieceColor.BLACK && !whiteTurn);
    }

    private void checkChess(int newX, int newY) {
        PieceColor enemyColor = chessPiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (chessPiece.selectedTeamIsInChess(new Point(newX, newY), enemyColor)) {
            if (enemyColor == PieceColor.WHITE) {
                whiteChess = true;
                System.out.println("Black says: CHESS!");
            } else {
                blackChess = true;
                System.out.println("White says: CHESS!");
            }
        }
    }

    public void printBoard(boolean[][] array) {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(array[j][i] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    private void revokeChess() {
        PieceColor enemyColor = chessPiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (enemyColor == PieceColor.WHITE) {
            blackChess = false;
            System.out.println("Black saved");
        } else {
            whiteChess = false;
            System.out.println("White saved");
        }
    }

    private boolean wasNotInChess() {
        return (chessPiece.getColor() == PieceColor.WHITE && !whiteChess)
                || (chessPiece.getColor() == PieceColor.BLACK && !blackChess);
    }

    public Component cannotMove() {
        chessPiece.setVisible(false);
        chessPiece.setLocation(chessPiece.getLastLocation());
        return chessBoard.findComponentAt(chessPiece.getLocation());
    }

    public void mouseClicked(MouseEvent e) {
        if (chessPiece != null) {
            if (chessPiece.getType() == PieceType.PAWN
                    && (chessPiece.getCurrentLocation().y == 1
                    || chessPiece.getCurrentLocation().y == 8)) {
                Piece queen = new QueenPiece(chessPiece.getColor(), 0, this);
                queen.setCurrentLocation(chessPiece.getLastLocation());
                queen.setSize(chessPiece.getWidth(), chessPiece.getHeight());
                queen.setIcon(queen.getImage());
                chessPiece.getParent().add(queen);
                pieces.add(queen);
                chessPiece.removeFrom(pieces);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }
}