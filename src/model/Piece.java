package model;

import controller.ChessBoard;
import utils.Functions;
import utils.PieceColor;
import utils.PieceType;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Egy bábut reprezentál és a hozzá közhető feladatokat látja el
 */
public abstract class Piece extends JLabel implements Serializable {
    protected final PieceColor color;
    protected final int boardLocation;
    protected Point lastLocation;
    protected Point currentLocation;
    protected final ChessBoard board;

    /**
     * Konstruktor
     *
     * @param color    a bábu színe
     * @param location a bábu pozíciója
     * @param board    a sakktábla amire a bábut helyezzük
     */
    public Piece(PieceColor color, int location, ChessBoard board) {
        // it centers the image
        super(Functions.getImage("blank.png"));
        this.color = color;
        this.boardLocation = location;
        this.board = board;
    }

    /**
     * Visszaadja a bábu pozícióját a sakktáblán sorszámként
     *
     * @return a sakktáblán lévő griden ezzel a sorszámmal szerepel
     */
    public int getBoardLocation() {
        return boardLocation;
    }

    /**
     * Elmenti a bábu legutóbbi pozícióját
     *
     * @param lastLocation a legutóbbi pozíció
     */
    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
    }

    /**
     * Visszaadja a bábu legutóbbi pozícióját
     *
     * @return a legutóbbi pozíció
     */
    public Point getLastLocation() {
        return lastLocation;
    }

    /**
     * Visszaadjaa bábu színét
     *
     * @return a bábu színe
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * Visszaadja a bábu ikonját
     *
     * @return a bábu ikonja
     */
    public ImageIcon getImage() {
        return color == PieceColor.WHITE ? getWhiteImage() : getBlackImage();
    }

    /**
     * Visszaadja a bábu lehetséges lépéseit
     *
     * @param include ezt a pontot is számügyre veszi (ide akar lépni az éppen körön lévő bábu)
     * @param exclude ezt a pontot nem veszi számításba (innen akar ellépni az éppen körön lévő bábu)
     * @return a lehetséges lépések
     */
    public abstract boolean[][] getMoves(Point include, Point exclude);

    /**
     * Visszaadja, hogy az adott helyre tud-e lépni a bábu
     *
     * @param newX    az új pont X koordinátája
     * @param newY    az új pont Y koordinátája
     * @param exclude ezt a pontot nem veszi figyelembe
     * @return tud-e az adott mezőre lépni a bábu
     */
    public boolean canMoveTo(int newX, int newY, Point exclude) {
        if (Functions.isOutside(newX, newY))
            return false;
        boolean[][] possibleMoves = getMoves(getCurrentLocation(), exclude);
        return possibleMoves[newX - 1][newY - 1];
    }

    /**
     * Visszaadja hogy tud-e lépni bárhova a bábu
     *
     * @return tud-e lépni a bábu
     */
    public boolean hasMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMoveTo(i, j, new Point(-1, -1)) && !selectedTeamIsInChess(new Point(i, j), getCurrentLocation(), getColor())) {
                    //System.out.println(i + 1 + " " + (j + 1));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Visszaadja egy véletlenszerűen választott lépését a bábu
     *
     * @return egy véletlenszerű mező ahova tud lépni
     */
    public Point pickRandomMove() {
        Point randomMove;
        Random random = new Random();
        boolean[][] moves = getMoves(getCurrentLocation(), new Point(-1, -1));
        ArrayList<Point> validMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (moves[j][i]) {
                    validMoves.add(new Point(j + 1, i + 1));
                }
            }
        }
        randomMove = validMoves.get(random.nextInt(validMoves.size()));
        return randomMove;
    }

    /**
     * Visszaadja hogy adott mezőn áll-e már adott színű bábu és lehetséges-e az odamozgás
     *
     * @param newX    az adott mező X koordinátája
     * @param newY    az adott mező Y koordinátája
     * @param color   az adott szín
     * @param include ezta mezőt is figyelembe veszi
     * @param exclude ezt a mezőt nem veszi figyelembe
     * @return szabálzys-e a lépés és van-e rajta adott színű bábu
     */
    public boolean isFreeFromColorAndValid(int newX, int newY, PieceColor color, Point include, Point exclude) {
        Point newPosition = new Point(newX, newY);
        if (Functions.isOutside(newPosition))
            return false;
        if (newPosition.equals(this.getCurrentLocation()))
            return false;
        for (Piece piece : getBoard().getPieces()) {
            if (((newPosition.equals(piece.getCurrentLocation())) || newPosition.equals(include))
                    && piece.getColor().equals(color)) {
                return newPosition.equals(exclude);
            }
        }
        return true;
    }

    /**
     * A ferde és vízszintes, hosszú lépéseket kezeli
     *
     * @param include ezt a mezőt is figyelembe veszi (ide akar lépni a körön lévő bábu)
     * @param exclude ezt a mezőt nem veszi figyelembe (innen akar ellépni a körön lévő bábu)
     * @param moves   a bábu lépései
     * @param relX    az x tengelyen akar-e lépni
     * @param relY    az y tengelyen akar-e lépni
     */
    protected void checkLongMoves(Point include, Point exclude, boolean[][] moves, int relX, int relY) {
        int offset = 1;
        while (true) {
            // relX&relY from {-1;+1}
            int newX = getCurrentLocation().x + offset * relX;
            int newY = getCurrentLocation().y + offset * relY;
            PieceColor enemyColor = this.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            if (!this.isFreeFromColorAndValid(newX, newY, enemyColor, include, exclude) && !Functions.isOutside(newX, newY)) {
                moves[newX - 1][newY - 1] = true;
                return;
            }
            if (this.isFreeFromColorAndValid(newX, newY, getColor(), include, exclude)) {
                moves[newX - 1][newY - 1] = true;
                offset++;
            } else {
                return;
            }
        }
    }

    /**
     * Megnézi hogy tud-e adott helyre lépni a bábu és ha igen, akkor elmenti lehetséges lépésként
     *
     * @param include ezt a mezőt is figyelembe veszi (ide akar lépni a körön lévő bábu)
     * @param exclude ezt a mezőt nem veszi figyelembe (innen akar ellépni a körön lévő bábu)
     * @param moves   ehhez a tömbhöz adja hozzá a további lehetséges lépéseket
     * @param relX    akar-e az X tengelyen mozogni
     * @param relY    akar-e az Y tengelyen mozogni
     */
    protected void checkMoves(Point include, Point exclude, boolean[][] moves, int relX, int relY) {
        int newX = getCurrentLocation().x + relX;
        int newY = getCurrentLocation().y + relY;
        if (this.isFreeFromColorAndValid(newX, newY, this.getColor(), include, exclude) && !Functions.isOutside(newX, newY)) {
            moves[newX - 1][newY - 1] = true;
        }
    }

    /**
     * Frissíti a bábu pozícióját
     *
     * @param newX   az új X koordináta
     * @param newY   az új Y koordináta
     * @param pieces a táblán lévő bábuk
     */
    public void placementUpdate(int newX, int newY, ArrayList<Piece> pieces) {
        // handle hits
        for (int i = 0; i < pieces.size(); i++) {
            if (!pieces.get(i).getColor().equals(this.getColor())) {
                if (pieces.get(i).getCurrentLocation().equals(new Point(newX, newY))) {
                    if (pieces.get(i).getType().equals(PieceType.KING)) {
                        //System.out.println("The king of alliance " + pieces.get(i).getColor() + " has fallen!");
                    }
                    pieces.get(i).removeFrom(pieces);
                }
            }
        }
        // move
        this.setCurrentLocation(newX, newY);
        this.setVisible(false);
    }

    /**
     * Hozzáadja a lehetséges lépéseit egy tömbhöz
     *
     * @param include ezt a mezőt is figyelembe veszi (ide akar lépni a körön lévő bábu)
     * @param exclude ezt a mezőt nem veszi figyelembe (innen akar ellépni a körön lévő bábu)
     * @param moves   ehhez adja hozzá a lépéseit
     * @return a frissített tömb
     */
    public boolean[][] addMovesTo(Point include, Point exclude, boolean[][] moves) {
        boolean[][] possibleMoves = getMoves(include, exclude);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possibleMoves[i][j]) {
                    moves[i][j] = true;
                }
            }
        }
        return moves;
    }

    /**
     * Visszaadja hogy az adott szín sakkban van-e
     *
     * @param include ezt a mezőt is figyelembe veszi (ide akar lépni a körön lévő bábu)
     * @param exclude ezt a mezőt nem veszi figyelembe (innen akar ellépni a körön lévő bábu)
     * @param color   az adott szín
     * @return sakkban van-e a szín
     */
    public boolean selectedTeamIsInChess(Point include, Point exclude, PieceColor color) {
        boolean[][] dangerZone = new boolean[8][8];

        for (Piece piece : getBoard().getPieces()) {
            if (!piece.getColor().equals(color)) {
                dangerZone = piece.addMovesTo(include, exclude, dangerZone);
            }
        }

        Piece king = getKing(color);

        if (king == null) return true;

        if (this.equals(king)) {
            if (dangerZone[include.x - 1][include.y - 1]) {
                //System.out.println("Your king is in danger");
                //getBoard().printBoard(dangerZone);
                return true;
            } else return false;
        } else if (dangerZone[king.getCurrentLocation().x - 1][king.getCurrentLocation().y - 1]) {
            //System.out.println("Your king is in danger");
            return true;
        } else return false;
    }

    /**
     * Visszaadja az adott szín királyának pozícióját
     *
     * @param color az aditt szín
     * @return a király pozíciója
     */
    private Piece getKing(PieceColor color) {
        for (Piece piece : getBoard().getPieces()) {
            if (piece.getType() == PieceType.KING && piece.getColor() == color) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Beállítja a bábu ikonját és jelenlegi pozícióját és hozzáadja a bábukhoz
     *
     * @param pieces ehhez adja hozzá
     */
    public void init(ArrayList<Piece> pieces) {
        this.setIcon(this.getImage());
        this.setCurrentLocation(new Point(this.getXLocationFromComponentNumber(), this.getYLocationFromComponentNumber()));
        pieces.add(this);
    }

    /**
     * Visszaadja a bábu X koordinátáját a griden lévő pozíciója alapján
     *
     * @return a bábu X pozíciója
     */
    public int getXLocationFromComponentNumber() {
        return boardLocation - (boardLocation / 8) * 8 + 1;
    }

    /**
     * Visszaadja a bábu Y koordinátáját a griden lévő pozíciója alapján
     *
     * @return a bábu Y pozíciója
     */
    public int getYLocationFromComponentNumber() {
        return boardLocation / 8 + 1;
    }

    public void setCurrentLocation(int newX, int newY) {
        currentLocation = new Point(newX, newY);
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public abstract ImageIcon getWhiteImage();

    public abstract ImageIcon getBlackImage();

    public void removeFrom(ArrayList<Piece> pieces) {
        if (pieces.contains(this)) {
            pieces.remove(this);
            this.setVisible(false);
            this.getParent().remove(this);
        }
    }

    public abstract PieceType getType();

    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return getType() + "{" +
                "color=" + color +
                ", currentLocation=" + currentLocation +
                '}';
    }
}