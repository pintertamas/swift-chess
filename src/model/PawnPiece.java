package model;

import controller.ChessBoard;
import utils.PieceColor;
import utils.Functions;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A gyalogokat reprezentálja
 */
public class PawnPiece extends Piece {
    boolean firstMove;

    public PawnPiece(PieceColor color, int location, ChessBoard board) {
        super(color, location, board);
        this.firstMove = true;
    }

    @Override
    public boolean[][] getMoves(Point include, Point exclude) {
        boolean[][] movingPoints = new boolean[8][8];
        Point from = getCurrentLocation();
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;
        int newYDoubleStep = from.y - 2 * direction;
        int newYSingleStep = from.y - direction;
        boolean isBlocked = false;
        if (newYSingleStep <= 8 && newYSingleStep > 0) {
            for (Piece piece : getBoard().getPieces()) {
                if (piece.getCurrentLocation().equals(new Point(from.x, newYSingleStep))) {
                    isBlocked = true;
                    break;
                }
            }
            movingPoints[from.x - 1][newYSingleStep - 1] = !isBlocked;
        }
        if (firstMove && newYDoubleStep <= 8 && newYDoubleStep > 0) {
            for (Piece piece : getBoard().getPieces()) {
                if (piece.getCurrentLocation().equals(new Point(from.x, newYDoubleStep))) {
                    isBlocked = true;
                    break;
                }
            }
            movingPoints[from.x - 1][newYDoubleStep - 1] = !isBlocked;
        }
        int newX = from.x - 1;
        handleObliqueMoves(include, exclude, movingPoints, newYSingleStep, newX);
        newX = from.x + 1;
        handleObliqueMoves(include, exclude, movingPoints, newYSingleStep, newX);

        return movingPoints;
    }

    @Override
    public boolean[][] addMovesTo(Point include, Point exclude, boolean[][] moves) {
        boolean[][] hittingMoves = super.addMovesTo(include, exclude, moves);
        int direction = getColor() == PieceColor.WHITE ? 1 : -1;

        if (!Functions.isOutside(getCurrentLocation().y - direction))
            hittingMoves[getCurrentLocation().y - direction - 1][getCurrentLocation().x - 1] = false;

        if (!Functions.isOutside(getCurrentLocation().y - 2 * direction))
            hittingMoves[getCurrentLocation().y - 2 * direction - 1][getCurrentLocation().x - 1] = false;

        return hittingMoves;
    }

    /**
     * Kezeli a ferde lépéseket mivel a gyalogok nem tudnak arra lépni, csak ha ütnek is
     * @param include ezt a mezőt is figyelembe veszi (ide akar lépni a körön lévő bábu)
     * @param exclude ezt a mezőt nem veszi figyelembe (innen akar ellépni a körön lévő bábu)
     * @param movingPoints ehhez a tömbhöz adja hozzá az esetleges plusz lépéseket
     * @param newYSingleStep ennyit lép az Y tengelyen
     * @param newX ennyit lép az X tengelyen
     */
    private void handleObliqueMoves(Point include, Point exclude, boolean[][] movingPoints, int newYSingleStep, int newX) {
        if (!Functions.isOutside(newX, newYSingleStep)) {
            for (Piece piece : getBoard().getPieces()) {
                if (piece.getColor() != getColor()
                        && piece.getCurrentLocation().equals(new Point(newX, newYSingleStep))
                        && !exclude.equals(new Point(newX, newYSingleStep))
                        || include.equals(new Point(newX, newYSingleStep))) {
                    movingPoints[newX - 1][newYSingleStep - 1] = true;
                    break;
                }
            }
        }
    }

    @Override
    public void placementUpdate(int newX, int newY, ArrayList<Piece> pieces) {
        super.placementUpdate(newX, newY, pieces);
        this.firstMove = false;
    }

    @Override
    public ImageIcon getWhiteImage() {
        return Functions.getImage("pawn_white.png");
    }

    @Override
    public ImageIcon getBlackImage() {
        return Functions.getImage("pawn_black.png");
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}
