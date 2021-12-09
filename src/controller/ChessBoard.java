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

/**
 * A sakktáblán történő dolgokért felelős osztály
 */
public class ChessBoard extends JFrame implements MouseListener, MouseMotionListener, Serializable {
    private final JLayeredPane layeredPane;
    private JPanel chessBoard;
    private Piece chessPiece;
    private int xAdjustment;
    private int yAdjustment;
    private ArrayList<Piece> pieces;
    private boolean whiteTurn;
    private boolean whiteChess;
    private boolean blackChess;
    private final boolean againstRobot;

    /**
     * Az osztály konstruktora
     *
     * @param againstRobot itt adható meg hogy robot ellen akarunk-e játszani
     */
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

    /**
     * Hozzáadja a bábukhoz a fehér bábukat
     */
    private void addWhitePieces() {
        addPawns(PieceColor.WHITE, 48);
        addRoyalFamily(PieceColor.WHITE, 60, 59);
        addBishops(PieceColor.WHITE, 58, 61);
        addKnights(PieceColor.WHITE, 57, 62);
        addRooks(PieceColor.WHITE, 56, 63);
    }

    /**
     * Hozzáadja a bábukhoz a fekete bábukat
     */
    private void addBlackPieces() {
        addPawns(PieceColor.BLACK, 8);
        addRoyalFamily(PieceColor.BLACK, 4, 3);
        addBishops(PieceColor.BLACK, 2, 5);
        addKnights(PieceColor.BLACK, 1, 6);
        addRooks(PieceColor.BLACK, 0, 7);
    }

    /**
     * Hozzáadja a sakktáblához, ezzel pedig kirajzolja a bábukat a képernyőre
     */
    public void drawPieces() {
        for (Piece piece : getPieces()) {
            JPanel panel = (JPanel) getChessBoard().getComponent(piece.getBoardLocation());
            panel.add(piece);
        }
    }

    /**
     * Létrehozza a gyalogokat
     *
     * @param color a gyalogok színe
     * @param from  ettől a mezőtől kezdve pakol fel 8 darabot
     */
    private void addPawns(PieceColor color, int from) {
        Piece piece;
        for (int i = from; i < from + 8; i++) {
            piece = new PawnPiece(color, i, this);
            piece.init(getPieces());
        }
    }

    /**
     * Létrehozza a királyt és a királynőt
     *
     * @param color         a színük
     * @param kingLocation  a király pozíciója
     * @param queenLocation a királynő pozíciója
     */
    private void addRoyalFamily(PieceColor color, int kingLocation, int queenLocation) {
        Piece king = new KingPiece(color, kingLocation, this);
        Piece queen = new QueenPiece(color, queenLocation, this);
        king.init(getPieces());
        queen.init(getPieces());
    }

    /**
     * Létrehozza a futókat
     *
     * @param color                a futók színe
     * @param firstBishopLocation  az egyik futó pozíciója
     * @param secondBishopLocation a másik futó pozíciója
     */
    private void addBishops(PieceColor color, int firstBishopLocation, int secondBishopLocation) {
        Piece firstBishop = new BishopPiece(color, firstBishopLocation, this);
        Piece secondBishop = new BishopPiece(color, secondBishopLocation, this);
        firstBishop.init(getPieces());
        secondBishop.init(getPieces());
    }

    /**
     * Létrehozza a bástyákat
     *
     * @param color              a bástyák színe
     * @param firstRookLocation  az egyik bástya pozíciója
     * @param secondRookLocation a másik bástya pozíiója
     */
    private void addRooks(PieceColor color, int firstRookLocation, int secondRookLocation) {
        Piece firstRook = new RookPiece(color, firstRookLocation, this);
        Piece secondRook = new RookPiece(color, secondRookLocation, this);
        firstRook.init(getPieces());
        secondRook.init(getPieces());
    }

    /**
     * Létrehozza a lovakat
     *
     * @param color                a lovak színe
     * @param firstBishopLocation  az egyik ló pozíciója
     * @param secondBishopLocation a másik ló pozíciója
     */
    private void addKnights(PieceColor color, int firstBishopLocation, int secondBishopLocation) {
        Piece firstBishop = new KnightPiece(color, firstBishopLocation, this);
        Piece secondBishop = new KnightPiece(color, secondBishopLocation, this);
        firstBishop.init(getPieces());
        secondBishop.init(getPieces());
    }

    /**
     * Kiválaszt véletlenszerűen egy bábut a sakktábláról
     *
     * @return a véletlenszerű bábut
     */
    private Piece pickRandomPiece() {
        Random random = new Random();
        Piece randomPiece = getPieces().get(random.nextInt(getPieces().size()));
        while (!(randomPiece.hasMoves() && randomPiece.getColor().equals(PieceColor.BLACK))) {
            randomPiece = getPieces().get(random.nextInt(getPieces().size()));
        }
        System.out.println(randomPiece);
        return randomPiece;
    }

    /**
     * Lekezeli az egérkattintás eseményét
     *
     * @param e az esemény
     */
    public void mousePressed(MouseEvent e) {
        handleBeginningOfMove(e.getX(), e.getY());
    }

    /**
     * Ez a függvény intézi a körök elején lévő bábuválasztást
     *
     * @param pixelX a választani kívánt bábu X pozíciója a sakktáblán pixelben
     * @param pixelY a választani kívánt bábu Y pozíciója a sakktáblán pixelben
     */
    private void handleBeginningOfMove(int pixelX, int pixelY) {
        setChessPiece(null);
        Component c = getChessBoard().findComponentAt(pixelX, pixelY);

        if (c.getClass().equals(JPanel.class))
            return;

        Point parentLocation = c.getParent().getLocation();
        setxAdjustment(parentLocation.x - pixelX);
        setyAdjustment(parentLocation.y - pixelY);
        setChessPiece((Piece) c);
        getChessPiece().setLocation(pixelX + getXadjustment(), pixelY + getYadjustment());
        getChessPiece().setLastLocation(getChessPiece().getLocation());
        getChessPiece().setSize(getChessPiece().getWidth(), getChessPiece().getHeight());
        getLayeredPane().add(getChessPiece(), JLayeredPane.DRAG_LAYER);
    }

    /**
     * Lekezeli az egér húzás eseményét
     *
     * @param e az esemény
     */
    public void mouseDragged(MouseEvent e) {
        if (getChessPiece() == null) return;
        getChessPiece().setLocation(e.getX() + getXadjustment(), e.getY() + getYadjustment());
    }

    /**
     * Lekezeli az egér felengedésének eseményét
     *
     * @param e az esemény
     */
    public void mouseReleased(MouseEvent e) {
        if (!isAgainstRobot() || isWhiteTurn()) {
            if (getChessPiece() == null) return;
            int newX = Functions.getLocationOnX(e.getX(), getChessBoard().getSize().width);
            int newY = Functions.getLocationOnY(e.getY(), getChessBoard().getSize().height);
            handleMove(newX, newY, e.getX(), e.getY());
        }
        if (isAgainstRobot() && !isWhiteTurn()) {
            handleRobotMove();
        }
    }

    /**
     * A robot mozgásáért felelős
     */
    private void handleRobotMove() {
        Piece randomPiece = pickRandomPiece();
        Point randomPieceCurrentLocation = randomPiece.getCurrentLocation();
        Point randomPieceNextLocation = randomPiece.pickRandomMove();
        try {
            handleBeginningOfMove(
                    randomPieceCurrentLocation.x * 80 - 40,
                    randomPieceCurrentLocation.y * 80 - 40);
            handleMove(
                    randomPieceNextLocation.x,
                    randomPieceNextLocation.y,
                    randomPieceNextLocation.x * 80 - 40,
                    randomPieceNextLocation.y * 80 - 40);
        } catch (Exception e) {
            System.out.println("Error");
            setWhiteTurn(false); //try again
        }
    }

    /**
     * Egy bábu mozgásáért felelős
     *
     * @param newX   a kívánt új pozíció X pozíciója koordinátában
     * @param newY   a kívánt új pozíció Y pozíciója koordinátában
     * @param pixelX a kívánt új pozíció X pozíciója pixelben
     * @param pixelY a kívánt új pozíció Y pozíciója pixelben
     */
    private void handleMove(int newX, int newY, int pixelX, int pixelY) {
        checkIfLost();

        Component component;

        if ((isYourTurn()
                && getChessPiece().canMoveTo(newX, newY, getChessPiece().getCurrentLocation()))) {
            //PieceColor enemyColor = getChessPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            if ((wasNotInChess() &&
                    (!getChessPiece().selectedTeamIsInChess(new Point(newX, newY), getChessPiece().getCurrentLocation(), getChessPiece().getColor())))) {
                checkChess(newX, newY);
                getChessPiece().placementUpdate(newX, newY, getPieces());
                component = getChessBoard().findComponentAt(pixelX, pixelY);
                setWhiteTurn(!isWhiteTurn());
            } else if (!wasNotInChess()
                    && !getChessPiece().selectedTeamIsInChess(new Point(newX, newY), getChessPiece().getCurrentLocation(), getChessPiece().getColor())) {
                revokeChess();
                getChessPiece().placementUpdate(newX, newY, getPieces());
                component = getChessBoard().findComponentAt(pixelX, pixelY);
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
            checkIfLost();
        }
        Container parent = (Container) component;
        parent.add(getChessPiece());
        checkIfLost();
        changePawnToQueen();
        getChessPiece().setVisible(true);
        checkChess(getChessPiece().getCurrentLocation().x, getChessPiece().getCurrentLocation().y);
    }

    /**
     * Visszaadja hogy az adott bábu köre van-e éppen
     *
     * @return az adott bábu köre van-e éppen
     */
    private boolean isYourTurn() {
        return getChessPiece().getColor() == PieceColor.WHITE && isWhiteTurn() || (getChessPiece().getColor() == PieceColor.BLACK && !isWhiteTurn());
    }

    /**
     * Megnézi hogy sakk alakult-e ki és ennek megfelelően beállítja a változókat
     *
     * @param newX a pozíció amiben nézi X koordinázája
     * @param newY a pozíció amiben nézi Y koordinázája
     */
    private void checkChess(int newX, int newY) {
        PieceColor enemyColor = getChessPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (getChessPiece().selectedTeamIsInChess(new Point(newX, newY), getChessPiece().getCurrentLocation(), enemyColor)) {
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
                System.out.print(array[j][i] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    private void checkIfLost() {
        if (checkIfTeamLost(PieceColor.WHITE)) {
            System.out.println("White lost");
        }
        if (checkIfTeamLost(PieceColor.BLACK)) {
            System.out.println("Black lost");
        }
    }

    private boolean checkIfTeamLost(PieceColor color) {
        boolean teamLost = true;
        for (Piece piece : getPieces()) {
            if (piece.getColor().equals(color) && piece.hasMoves()) {
                System.out.println(piece.getCurrentLocation());
                teamLost = false;
                break;
            }
        }
        if (teamLost)
            System.out.println("asdasd");
        return teamLost;
    }

    /**
     * Visszavonja a sakkot a csapatának
     */
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

    /**
     * Visszaadja hogy sakkban állt-e eddig a bábu
     *
     * @return sakkban állt-e a bábu
     */
    private boolean wasNotInChess() {
        return (getChessPiece().getColor() == PieceColor.WHITE && !isWhiteChess())
                || (getChessPiece().getColor() == PieceColor.BLACK && !isBlackChess());
    }

    /**
     * Lekezeli az egérkattintás eseményét
     *
     * @param e az esemény
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Lecseréli a gyalogot királynőre
     */
    private void changePawnToQueen() {
        if (getChessPiece() != null) {
            if (getChessPiece().getType() == PieceType.PAWN
                    && (getChessPiece().getCurrentLocation().y == 1
                    || getChessPiece().getCurrentLocation().y == 8)) {
                Piece queen = new QueenPiece(getChessPiece().getColor(), getChessPiece().getBoardLocation(), getChessPiece().getBoard());
                queen.init(getPieces());
                getChessPiece().getParent().add(queen);
                getChessPiece().removeFrom(getPieces());
            }
        }
    }

    /**
     * Lekezeli az egér mozgatás eseményét
     *
     * @param e az esemény
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Lekezeli az eseményt amikor az egér bekerül a képernyőre
     *
     * @param e az esemény
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Lekezeli az eseményt amikor az egér elhagyja a képernyőt
     *
     * @param e az esemény
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Billentyű lenyomások érzékeléséhez készített belső osztály
     */
    private class CustomKeyListener extends KeyAdapter implements Serializable {
        ChessBoard parentFrame;
        String message = "";

        /**
         * Konstruktor
         *
         * @param parentFrame a sakktábla amire rá akarjuk rakni
         */
        public CustomKeyListener(ChessBoard parentFrame) {
            this.parentFrame = parentFrame;
        }

        /**
         * Lekezeli a billentyű lenyomásának eseményét
         *
         * @param e az esemény
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 87) {
                setMessage("White surrendered!");
                JOptionPane.showMessageDialog(getChessBoard(), getMessage());
                Database.saveGame(new ChessBoard(isAgainstRobot()));
                parentFrame.dispose();
            } else if (e.getKeyCode() == 66) {
                setMessage("Black surrendered!");
                JOptionPane.showMessageDialog(getChessBoard(), getMessage());
                Database.saveGame(new ChessBoard(isAgainstRobot()));
                parentFrame.dispose();
            }
        }

        /**
         * A message getter függvénye
         *
         * @return a kiírni kívánt üzenet
         */
        public String getMessage() {
            return message;
        }

        /**
         * A message setter függvénye
         *
         * @param message az üzenet
         */
        public void setMessage(String message) {
            this.message = message;
        }
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