package db;

import controller.ChessBoard;

import java.io.*;

public class Database {
    final static String fileName = "savedChessGame.ser";

    public ChessBoard loadGame(boolean againstRobot) {
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

    public void saveGame(ChessBoard chessBoard) {
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