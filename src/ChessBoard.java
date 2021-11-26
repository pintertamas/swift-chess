import model.KingPiece;
import model.PawnPiece;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

import model.Piece;
import model.QueenPiece;
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
        drawPieces(panel, pieces);
    }

    private void addWhitePieces() {
        addPawns(pieces, PieceColor.WHITE, 48);
        addRoyalFamily(pieces, PieceColor.WHITE, 59, 60);
    }

    private void addBlackPieces() {
        addPawns(pieces, PieceColor.BLACK, 8);
        addRoyalFamily(pieces, PieceColor.BLACK, 4, 3);
    }


    private void drawPieces(JPanel panel, ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            panel = (JPanel) chessBoard.getComponent(piece.getBoardLocation());
            panel.add(piece);
        }
    }

    private void addPawns(ArrayList<Piece> pieces, PieceColor color, int from) {
        Piece piece;
        for (int i = from; i < from + 8; i++) {
            piece = new PawnPiece(color, i);
            piece.init(pieces);
        }
    }

    private void addRoyalFamily(ArrayList<Piece> pieces, PieceColor color, int kingLocation, int queenLocation) {
        Piece king = new KingPiece(color, kingLocation);
        Piece queen = new QueenPiece(color, queenLocation);
        king.init(pieces);
        queen.init(pieces);
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

        if ((isYourTurn()
                && chessPiece.canMoveTo(newX, newY, pieces)
                && chessPiece.freeToMove(newX, newY, pieces))) {
            if (wasNotInChess() && (!chessPiece.causesChessToSelectedTeam(new Point(newX, newY), pieces, chessPiece.getColor()))) {
                giveChess(newX, newY);
            }
            if (!wasNotInChess() && chessPiece.possibleToStopChess(new Point(newX, newY), pieces)) {
                System.out.println("Saved!");
                revokeChess(newX, newY);
            }
            chessPiece.handleHits(newX, newY, pieces);
            chessPiece.setVisible(false);
            component = chessBoard.findComponentAt(e.getX(), e.getY());
            chessPiece.setCurrentLocation(newX, newY);
            whiteTurn = !whiteTurn;
        } else {
            component = cannotMove();
        }
        Container parent = (Container) component;
        parent.add(chessPiece);
        chessPiece.setVisible(true);
    }

    private boolean isYourTurn() {
        return chessPiece.getColor() == PieceColor.WHITE && whiteTurn || (chessPiece.getColor() == PieceColor.BLACK && !whiteTurn);
    }

    private void giveChess(int newX, int newY) {
        System.out.println("Give chess");
        PieceColor enemyColor = chessPiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (chessPiece.causesChessToSelectedTeam(new Point(newX, newY), pieces, enemyColor)) {
            if (enemyColor == PieceColor.WHITE) {
                whiteChess = true;
                System.out.println("Black says: CHESS!");
            } else {
                blackChess = true;
                System.out.println("White says: CHESS!");
            }
        }
    }

    private void revokeChess(int newX, int newY) {
        PieceColor enemyColor = chessPiece.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (chessPiece.causesChessToSelectedTeam(new Point(newX, newY), pieces, enemyColor)) {
            if (enemyColor == PieceColor.WHITE) {
                whiteChess = false;
                System.out.println("Black saved");
            } else {
                blackChess = false;
                System.out.println("White saved");
            }
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

    public void printBoard(boolean[][] dangerZone) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(dangerZone[j][i] ? 1 + " " : 0 + " ");
            }
            System.out.println();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (chessPiece != null) {
            Piece queen = new QueenPiece(chessPiece.getColor(), 0);
            queen.setCurrentLocation(chessPiece.getLastLocation());
            queen.setSize(chessPiece.getWidth(), chessPiece.getHeight());
            queen.setIcon(queen.getImage());
            chessPiece.getParent().add(queen);
            pieces.add(queen);
            chessPiece.removeFrom(pieces);
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}