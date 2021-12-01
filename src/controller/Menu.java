package controller;

import db.Database;
import utils.StyledButtonUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Menu extends JFrame {

    JButton loadButton;
    JButton newButton;
    JButton exitButton;
    Checkbox againstRobot;

    public Menu() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu");
            frame.add(new MenuPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    public class MenuPane extends JPanel {

        public MenuPane() {
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            add(new JLabel("<html><h1><strong>SwiftChess Menu</strong></h1><hr></html>"), gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            newButton = new JButton("Start");
            againstRobot = new Checkbox("Against Robot?");
            loadButton = new JButton("Load");
            exitButton = new JButton("Exit");
            newButton.setUI(new StyledButtonUI());
            loadButton.setUI(new StyledButtonUI());
            exitButton.setUI(new StyledButtonUI());
            newButton.addActionListener(e -> {

                ChessBoard chessBoard = new ChessBoard(againstRobot.getState());
                setOnClose(chessBoard);
            });
            loadButton.addActionListener(e -> {
                ChessBoard chessBoard = new Database().loadGame(againstRobot.getState());
                setOnClose(chessBoard);
            });
            exitButton.addActionListener(e -> {
                System.exit(0);
            });

            gbc.weighty = 1;
            add(newButton, gbc);
            add(againstRobot, gbc);
            add(loadButton, gbc);
            add(exitButton, gbc);
        }

        private void setOnClose(ChessBoard chessBoard) {
            chessBoard.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    new Database().saveGame(chessBoard);
                }
            });
            chessBoard.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            chessBoard.pack();
            chessBoard.setResizable(false);
            chessBoard.setLocationRelativeTo(null);
            chessBoard.setVisible(true);
        }

    }
}
