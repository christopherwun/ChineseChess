package org.cis120.chinesechess;

import javax.swing.*;
import java.awt.*;

import static org.cis120.chinesechess.ChessBoard.BOARD_HEIGHT;
import static org.cis120.chinesechess.ChessBoard.BOARD_WIDTH;

public class RunChineseChess implements Runnable {
    private JFrame instructions;

    public void run() {
        runChess();
        setupInstructions();
        showInstructions();
    }

    public void runChess() {
        // Frame for game components
        final JFrame frame = new JFrame("Chinese Chess");
        frame.setLocation(0, 0);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        // Status panel for status updates
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("");
        status_panel.add(status);

        // Initialize game board and add it to the frame
        final org.cis120.chinesechess.ChessBoard board = new ChessBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // New game button to revert game to original state
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("New Game");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> showInstructions());
        control_panel.add(instructions);

        // Reset the board to start the game.
        board.reset();
    }

    public void setupInstructions() {
        // Frame for instructions
        final JFrame frame = new JFrame("Instructions");
        frame.setLocation(0, 0);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        // Status panel for status updates
        final JPanel text_panel = new JPanel();
        text_panel.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        frame.add(text_panel, BorderLayout.CENTER);

        final JLabel status = new JLabel(
                "<html>" +
                        "<h1>Chinese Chess (象棋)</h1>" +
                        "<h2>How to play:</h2>" +
                        "<p>Red goes first. Select a piece to move, then move it to a new " +
                        "position. The game will</p>"
                        +
                        "<p>inform you if your move is illegal. Illegal moves include moves " +
                        "that are not allowed</p>"
                        +
                        "<p>by a piece's move and capture rules (see below) or that leave " +
                        "your general in check</p>"
                        +
                        "<p>(when it can be captured on the next turn by an opposing piece. " +
                        "Click anywhere besides</p>"
                        +
                        "<p>a legal move to deselect the piece and select a new one.</p>" +
                        "<h2>Ending the game:</h2>" +
                        "<p>The game ends when one side has no more legal moves to make on " +
                        "their turn.</p>"
                        +
                        "<h2>Move and Capture Rules:</h2>" +
                        "<table>" +
                        "<thead>" +
                        "<tr>" +
                        "<td>Soldier (兵,卒)</td>" +
                        "<td>" +
                        "<p>Their motion and capture is in single steps forward in the start " +
                        "of the</p>"
                        +
                        "<p>game. After crossing the river (midline of the board), they can " +
                        "move and</p>"
                        +
                        "<p>capture to the left and right as well.</p>" +
                        "</td>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>" +
                        "<tr>" +
                        "<td>Cannon (炮,砲)</td>" +
                        "<td>" +
                        "<p>Their motion is in horizontal and vertical lines until collision " +
                        "with</p>"
                        +
                        "<p>another piece. They can only capture pieces in horizontal/vertical " +
                        "directions</p>"
                        +
                        "<p>after jumping over another piece. They cannot jump over pieces " +
                        "without</p>"
                        +
                        "<p>capturing and cannot capture without jumping over pieces.</p>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Chariot (車)</td>" +
                        "<td>" +
                        "<p>Their motion is in horizontal and vertical lines until collision " +
                        "with</p>"
                        +
                        "<p>another piece. They can capture the first piece they collide with. " +
                        "They</p>"
                        +
                        "<p>cannot jump over other pieces to move or capture.</p>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Horse (馬)</td>" +
                        "<td>" +
                        "<p>Their motion is one step either horizontally or vertically in " +
                        "any</p>" +
                        "<p>direction, then a diagonal step continuing in that direction " +
                        "(one</p>" +
                        "<p>horizontal, one vertical). They may not capture or move through " +
                        "pieces on</p>"
                        +
                        "<p>their first step. They can capture pieces on their second " +
                        "step.</p>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Elephant (相,象)</td>" +
                        "<td>" +
                        "<p>Their motion and is two steps diagonally in any direction (two</p>" +
                        "<p>horizontal + two vertical in any combination). They cannot jump " +
                        "over</p>"
                        +
                        "<p>pieces in their way, and can only capture pieces at the end of " +
                        "their</p>"
                        +
                        "<p>diagonal movement.</p>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Guard (士,仕)</td>" +
                        "<td>" +
                        "<p>They can move and capture one step diagonally in any direction " +
                        "(one</p>"
                        +
                        "<p>horizontal + one vertical in any combination). They cannot leave " +
                        "their</p>"
                        +
                        "<p>general's palace (middle square on their side of the field).</p>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>General (帥,將)</td>" +
                        "<td>" +
                        "<p>They can move and capture one step horizontally or vertically in " +
                        "any</p>"
                        +
                        "<p>direction. They cannot leave their palace</p>" +
                        "</td>" +
                        "</tr>" +
                        "</tbody>" +
                        "</table>" +
                        "</html>"
        );
        status.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        text_panel.add(status);

        // set variable to frame
        instructions = frame;
    }

    public void showInstructions() {
        // Put the frame on the screen
        instructions.pack();
        instructions.setVisible(true);
    }
}
