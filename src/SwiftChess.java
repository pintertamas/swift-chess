import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class SwiftChess {
    public static void main(String[] args) {
        JFrame frame = new ChessBoard();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}