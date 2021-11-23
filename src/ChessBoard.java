import model.PawnPiece;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

import model.Piece;
import utils.*;

import javax.swing.*;

public class ChessBoard extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    Piece chessPiece;
    int xAdjustment;
    int yAdjustment;
    ArrayList<Piece> pieces;

    public ChessBoard() {
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
                if (i % 2 == 0) square.setBackground(j % 2 == 0 ? darkColor : lightColor);
                else square.setBackground(j % 2 == 0 ? lightColor : darkColor);
            }
        }

        JPanel panel = (JPanel) chessBoard.getComponent(0);
        initWhitePieces(panel);
        initBlackPieces(panel);
        drawPieces(panel, pieces);
    }

    private void initWhitePieces(JPanel panel) {
        addPawns(pieces, PieceColor.WHITE, 8);
    }

    private void initBlackPieces(JPanel panel) {
        addPawns(pieces, PieceColor.BLACK, 48);
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
            piece.setIcon(piece.getImage());
            pieces.add(piece);
        }
    }

    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        System.out.println(e.getX() + " " + e.getY());

        if (c.getClass().equals(JPanel.class))
            return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (Piece) c;
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setLastLocation(chessPiece.getLocation());
        System.out.println(((Piece) c).getLocationOnX());
        System.out.println(((Piece) c).getLocationOnY());
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
        System.out.println("new x-y " + newX + "-" + newY);
        Component component;

        if (chessPiece.canMoveTo(newX, newY)) {
            chessPiece.setVisible(false);
            component = chessBoard.findComponentAt(e.getX(), e.getY());
        } else {
            chessPiece.setVisible(false);
            chessPiece.setLocation(chessPiece.getLastLocation());
            component = chessBoard.findComponentAt(chessPiece.getLocation());
        }
        Container parent;

        if (component.getClass().equals(JFrame.class)) {
            parent = component.getParent();
            System.out.println(e.getX() + " " + e.getY());
            System.out.println("removed component: " + parent.getComponent(0));
            parent.remove(0);
        } else {
            parent = (Container) component;
        }
        parent.add(chessPiece);
        chessPiece.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}