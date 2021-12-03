package db;

import controller.ChessBoard;

import java.io.*;

/**
 * Adatbázis a játék elmentésére és visszatöltésére
 */
public class Database {
    /**
     * Ebben a fájlban tárolja a szerializált játékot
     */
    final static String fileName = "savedChessGame.ser";

    /**
     * Visszatölti a játékot a fájlnév alapján (ha nem tudja, létrehoz egy újat)
     * @param againstRobot robot ellen megy-e a játék
     * @return a betöltött játék
     */
    public static ChessBoard loadGame(boolean againstRobot) {
        File f = new File(fileName);
        if (f.exists()) {
            try {
                ChessBoard tmp;
                FileInputStream fileIn = new FileInputStream(fileName);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                tmp = (ChessBoard) in.readObject();
                in.close();
                fileIn.close();
                return tmp;
            } catch (ClassNotFoundException | IOException i) {
                i.printStackTrace();
            }
        }
        return new ChessBoard(againstRobot);
    }

    /**
     * Elmenti a játékot
     * @param chessBoard ezt menti el
     */
    public static void saveGame(ChessBoard chessBoard) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(chessBoard);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}