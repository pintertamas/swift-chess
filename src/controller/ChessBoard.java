package controller;

import db.Database;
import model.*;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import utils.*;

import javax.swing.*;

public class ChessBoard extends JFrame implements MouseListener, MouseMotionListener, Serializable {
    final JLayeredPane layeredPane;
    JPanel chessBoard;
    Piece chessPiece;
    int xAdjustment;
    int yAdjustment;
    ArrayList<Piece> pieces;
    boolean whiteTurn;
    boolean whiteChess;
    boolean blackChess;
    boolean againstRobot;

    private class CustomKeyListener extends KeyAdapter implements Serializable {
        ChessBoard parentFrame;
        String message = "";

        public CustomKeyListener(ChessBoard parentFrame) {
            this.parentFrame = parentFrame;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 87) {
                setMessage("White surrendered!");
            } else if (e.getKeyCode() == 66) {
                setMessage("Black surrendered!");
            }
            JOptionPane.showMessageDialog(getChessBoard(), getMessage());
            new Database().saveGame(new ChessBoard(isAgainstRobot()));
            parentFrame.dispose();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public ChessBoard(boolean againstRobot) {
        this.againstRobot = againstRobot;
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
        this.setFocusable(true);
        this.requestFocus();
        CustomKeyListener listener = new CustomKeyListener(this);
        this.addKeyListener(listener);
        pieces = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel(new BorderLayout());
                getChessBoard().add(square);
                Color lightColor = new Color(234, 216, 186);
                Color darkColor = new Color(174, 133, 106);
                if (i % 2 == 0) square.setBackground(j % 2 == 0 ? lightColor : darkColor);
                else square.setBackground(j % 2 == 0 ? darkColor : lightColor);
            }
        }

        addWhitePieces();
        addBlackPieces();
        drawPieces();
    }

    private void addWhitePieces() {
        addPawns(PieceColor.WHITE, 48);
        addRoyalFamily(PieceColor.WHITE, 59, 60);
        addBishops(PieceColor.WHITE, 58, 61);
        addKnights(PieceColor.WHITE, 57, 62);
        addRooks(PieceColor.WHITE, 56, 63);
    }

    private void addBlackPieces() {
        addPawns(PieceColor.BLACK, 8);
        addRoyalFamily(PieceColor.BLACK, 3, 4);
        addBishops(PieceColor.BLACK, 2, 5);
        addKnights(PieceColor.BLACK, 1, 6);
        addRooks(PieceColor.BLACK, 0, 7);
    }

    private void drawPieces() {
        for (Piece piece : getPieces()) {
            JPanel panel = (JPanel) getChessBoard().getComponent(piece.getBoardLocation());
            panel.add(piece);
        }
    }

    private void addPawns(PieceColor color, int from) {
        Piece piece;
        for (int i = from; i < from + 8; i++) {
            piece = new PawnPiece(color, i, this);
            piece.init(getPieces());
        }
    }

    private void addRoyalFamily(PieceColor color, int kingLocation, int queenLocation) {
        Piece king = new KingPiece(color, kingLocation, this);
        Piece queen = new QueenPiece(color, queenLocation, this);
        king.init(getPieces());
        queen.init(getPieces());
    }

    private void addBishops(PieceColor color, int firstBishopLocation, int secondBishopLocation) {
        Piece firstBishop = new BishopPiece(color, firstBishopLocation, this);
        Piece secondBishop = new BishopPiece(color, secondBishopLocation, this);
        firstBishop.init(getPieces());
        secondBishop.init(getPieces());
    }

    private void addRooks(PieceColor color, int firstRookLocation, int secondRookLocation) {
        Piece firstRook = new RookPiece(color, firstRookLocation, this);
        Piece secondRook = new RookPiece(color, secondRookLocation, this);
        firstRook.init(getPieces());
        secondRook.init(getPieces());
    }

    private void addKnights(PieceColor color, int firstBishopLocation, int secondBishopLocation) {
        Piece firstBishop = new KnightPiece(color, firstBishopLocation, this);
        Piece secondBishop = new KnightPiece(color, secondBishopLocation, this);
        firstBishop.init(getPieces());
        secondBishop.init(getPieces());
    }

    private void handleRobotMove() {
        Piece randomPiece = pickRandomPiece();
        Point randomMove = randomPiece.pickRandomMove();
        Point currentLocationInPixels = new Point(
                randomPiece.getCurrentLocation().x * getWidth() / 8 + getWidth() / 4,
                randomPiece.getCurrentLocation().y * getHeight() / 8 + getHeight() / 4);
        Point randomPointInPixels = new Point(
                randomMove.x * getWidth() / 8 + getWidth() / 4,
                randomMove.y * getHeight() / 8 + getHeight() / 4);
        System.out.println(randomPiece);
        System.out.println(randomMove);
        System.out.println(randomPointInPixels);
    }

    private Piece pickRandomPiece() {
        Random random = new Random();
        Piece randomPiece = pieces.get(random.nextInt(pieces.size()));
        while (!(randomPiece.hasMoves() && randomPiece.getColor().equals(PieceColor.BLACK))) {
            randomPiece = pieces.get(random.nextInt(pieces.size()));
        }
        return randomPiece;
    }

    public void mousePressed(MouseEvent e) {
        setChessPiece(null);
        Component c = getChessBoard().findComponentAt(e.getX(), e.getY());

        if (c.getClass().equals(JPanel.class))
            return;

        Point parentLocation = c.getParent().getLocation();
        setxAdjustment(parentLocation.x - e.getX());
        setyAdjustment(parentLocation.y - e.getY());
        setChessPiece((Piece) c);
        getChessPiece().setLocation(e.getX() + getXadjustment(), e.getY() + getYadjustment());
        getChessPiece().setLastLocation(getChessPiece().getLocation());
        getChessPiece().setSize(getChessPiece().getWidth(), getChessPiece().getHeight());
        getLayeredPane().add(getChessPiece(), JLayeredPane.DRAG_LAYER);
    }

    public void mouseDragged(MouseEvent e) {
        if (getChessPiece() == null) return;
        getChessPiece().setLocation(e.getX() + getXadjustment(), e.getY() + getYadjustment());
    }

    public void mouseReleased(MouseEvent e) {
        if (getChessPiece() == null) return;
        int newX = Functions.getLocationOnX(e.getX(), getChessBoard().getSize().width);
        int newY = Functions.getLocationOnY(e.getY(), getChessBoard().getSize().height);
        Component component;

        if ((isYourTurn() && getChessPiece().canMoveTo(newX, newY, getChessPiece().getCurrentLocation()))) {
            PieceColor enemyColor = getChessPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

            if ((wasNotInChess() &&
                    (!getChessPiece().selectedTeamIsInChess(getChessPiece().getCurrentLocation(), getChessPiece().getCurrentLocation(), getChessPiece().getColor())
                            || (!getChessPiece().selectedTeamIsInChess(getChessPiece().getCurrentLocation(), getChessPiece().getCurrentLocation(), enemyColor))))) {
                checkChess(newX, newY);
                getChessPiece().placementUpdate(newX, newY, getPieces());
                component = getChessBoard().findComponentAt(e.getX(), e.getY());
                setWhiteTurn(!isWhiteTurn());
            } else if (!wasNotInChess() && getChessPiece().selectedTeamIsInChess(getChessPiece().getCurrentLocation(), new Point(newX, newY), getChessPiece().getColor())) {
                revokeChess();
                getChessPiece().placementUpdate(newX, newY, getPieces());
                component = getChessBoard().findComponentAt(e.getX(), e.getY());
                setWhiteTurn(!isWhiteTurn());
            } else {
                getChessPiece().setVisible(false);
                getChessPiece().setLocation(getChessPiece().getLastLocation());
                component = getChessBoard().findComponentAt(getChessPiece().getLocation());
            }
        } else {
            getChessPiece().setVisible(false);
            getChessPiece().setLocation(getChessPiece().getLastLocation());
            component = getChessBoard().findComponentAt(getChessPiece().getLocation());
        }
        Container parent = (Container) component;
        parent.add(getChessPiece());
        getChessPiece().setVisible(true);
        checkChess(getChessPiece().getCurrentLocation().x, getChessPiece().getCurrentLocation().y);
    }

    private boolean isYourTurn() {
        return getChessPiece().getColor() == PieceColor.WHITE && isWhiteTurn() || (getChessPiece().getColor() == PieceColor.BLACK && !isWhiteTurn());
    }

    private void checkChess(int newX, int newY) {
        PieceColor enemyColor = getChessPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (getChessPiece().selectedTeamIsInChess(getChessPiece().getCurrentLocation(), new Point(newX, newY), enemyColor)) {
            if (enemyColor == PieceColor.WHITE) {
                setWhiteChess(true);
                System.out.println("Black says: CHESS!");
            } else {
                setBlackChess(true);
                System.out.println("White says: CHESS!");
            }
        }
    }

    public void printBoard(boolean[][] array) {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(array[i][j] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    private void revokeChess() {
        PieceColor enemyColor = getChessPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (enemyColor == PieceColor.WHITE) {
            setBlackChess(false);
            System.out.println("Black saved");
        } else {
            setWhiteChess(false);
            System.out.println("White saved");
        }
    }

    private boolean wasNotInChess() {
        return (getChessPiece().getColor() == PieceColor.WHITE && !isWhiteChess())
                || (getChessPiece().getColor() == PieceColor.BLACK && !isBlackChess());
    }

    public void mouseClicked(MouseEvent e) {
        if (getChessPiece() != null) {
            if (getChessPiece().getType() == PieceType.PAWN
                    && (getChessPiece().getCurrentLocation().y == 1
                    || getChessPiece().getCurrentLocation().y == 8)) {
                Piece queen = new QueenPiece(getChessPiece().getColor(), 0, this);
                queen.setCurrentLocation(getChessPiece().getLastLocation());
                queen.setSize(getChessPiece().getWidth(), getChessPiece().getHeight());
                queen.setIcon(queen.getImage());
                getChessPiece().getParent().add(queen);
                getPieces().add(queen);
                getChessPiece().removeFrom(getPieces());
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

    public JPanel getChessBoard() {
        return chessBoard;
    }

    public Piece getChessPiece() {
        return chessPiece;
    }

    public int getXadjustment() {
        return xAdjustment;
    }

    public int getYadjustment() {
        return yAdjustment;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isWhiteChess() {
        return whiteChess;
    }

    public boolean isBlackChess() {
        return blackChess;
    }

    public void setChessPiece(Piece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public void setxAdjustment(int xAdjustment) {
        this.xAdjustment = xAdjustment;
    }

    public void setyAdjustment(int yAdjustment) {
        this.yAdjustment = yAdjustment;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
    }

    public void setWhiteChess(boolean whiteChess) {
        this.whiteChess = whiteChess;
    }

    public void setBlackChess(boolean blackChess) {
        this.blackChess = blackChess;
    }

    public boolean isAgainstRobot() {
        return againstRobot;
    }
}