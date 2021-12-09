package test.model;

import controller.ChessBoard;
import model.PawnPiece;
import model.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.PieceColor;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    ChessBoard chessBoard;
    Piece p0;
    Piece p1;
    Piece p2;
    Piece p3;

    @BeforeEach
    void setUp() {
        chessBoard = new ChessBoard(false);
        chessBoard.getPieces().removeAll(chessBoard.getPieces());
        p0 = new PawnPiece(PieceColor.BLACK, 0, getChessBoard());
        p1 = new PawnPiece(PieceColor.WHITE, 8, getChessBoard());
        p2 = new PawnPiece(PieceColor.WHITE, 9, getChessBoard());
        p3 = new PawnPiece(PieceColor.WHITE, 11, getChessBoard());
        p1.init(getChessBoard().getPieces());
        p2.init(getChessBoard().getPieces());
        p3.init(getChessBoard().getPieces());
    }

    /**
     * Leteszteli hogy jól állapítja-e meg a program hogy tud-e az adott mezőre lépni a bábu
     */
    @Test
    void canMoveTo() {
        Piece p4 = new PawnPiece(PieceColor.WHITE, 12, getChessBoard());
        p4.init(getChessBoard().getPieces());
        assertTrue(p4.canMoveTo(5, 1, new Point(-1, -1)));
    }

    /**
     * Leteszteli hogy jól állapytja-e meg a program hogy tud-e tovább lépni a bábu
     */
    @Test
    void hasMoves() {
        assertFalse(p3.hasMoves());
        Piece p4 = new PawnPiece(PieceColor.WHITE, 7, getChessBoard());
        p4.init(getChessBoard().getPieces());
        assertFalse(p4.hasMoves());
    }

    /**
     * Leteszteli hogy jól választ-e random lépést a program
     */
    @Test
    void pickRandomMove() {
        Point randomPoint = p3.pickRandomMove();
        assertEquals(new Point(4, 1), randomPoint);
        assertNotEquals(new Point(3, 1), randomPoint);
        assertNotEquals(new Point(5, 1), randomPoint);
    }

    /**
     * Leteszteli hogy jól állapítja-e meg a program hogy az adott mezőre léphet-e a bábu és van-e adott színű bábu a mezőn
     */
    @Test
    void isFreeFromColorAndValid() {
        assertTrue(p2.isFreeFromColorAndValid(2, 1, PieceColor.WHITE, new Point(-1, -1), new Point(-1, -1)));
    }

    /**
     * Leteszteli hogy jól detektálja-e a program hogy a választott csapat sakkban van-e
     */
    @Test
    void selectedTeamIsInChess() {
        assertTrue(p0.selectedTeamIsInChess(new Point(-1, -1), new Point(-1, -1), PieceColor.BLACK));
        assertTrue(p0.selectedTeamIsInChess(new Point(-1, -1), new Point(-1, -1), PieceColor.WHITE));
    }

    /**
     * Leteszteli hogy jól adja-e vissza a koordinátákat a program a griden található pozíció alapján
     */
    @Test
    void getXLocationFromComponentNumber() {
        assertEquals(p1.getCurrentLocation().x, p1.getXLocationFromComponentNumber());
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }
}