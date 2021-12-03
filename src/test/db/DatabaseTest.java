package test.db;

import controller.ChessBoard;
import db.Database;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void loadGame() {
        ChessBoard chessBoard = new ChessBoard(false);
        ChessBoard otherChessBoard = Database.loadGame(true);
        assertNotEquals(chessBoard, otherChessBoard);
    }

    @Test
    void saveGame() {
        ChessBoard chessBoard = new ChessBoard(false);
        Database.saveGame(chessBoard);
        assertEquals(chessBoard.getChessPiece(), Database.loadGame(false).getChessPiece());
    }
}