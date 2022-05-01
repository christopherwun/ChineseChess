package org.cis120.chinesechess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class creates a ChineseChess object, which is the model for the game.
 * When the user clicks the chess board, the model updates itself and the board
 * updates its status, then repaints itself in the game window.
 *
 * This game uses the Model-View-Controller framework, so it uses a
 * MouseListener
 * to manage interactions while also having a paint function and status JLabel
 * to
 * control the visuals in the game window.
 *
 */
public class ChessBoard extends JPanel {

    private final ChineseChess cc; // game instance
    private final JLabel status; // status text

    // game sizing is scaled depending on the SQUARE_SIZE
    public static final int SQUARE_SIZE = 60;
    public static final int BOARD_WIDTH = SQUARE_SIZE * 10;
    public static final int BOARD_HEIGHT = SQUARE_SIZE * 11;

    /**
     * Initializer for the game board
     *
     * @param statusInit starting status label
     */
    public ChessBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        cc = new ChineseChess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        setFocusable(true); // Enable keyboard focus on the court area.

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = toBoardCoords(new Point(e.getPoint()));
                if (inBounds(p)) {
                    // updates the model based on the coordinates of the mouseclick
                    cc.playMove(p);

                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }
            }
        });
    }

    /**
     * Resets the game to its initial state.
     */
    public void reset() {
        cc.reset();
        status.setText("Red Turn. Select a piece.");
        repaint();

        requestFocusInWindow(); // Makes sure this component has both keyboard and mouse focus

    }

    /**
     * Converts board coordinates ((0,0) to (8,9)) to graphics coordinates.
     *
     * @param p the point to convert (in board coordinates)
     * @return the converted point (in graphics coordinates)
     */
    public static Point toGCoords(Point p) {
        return (new Point((p.x + 1) * SQUARE_SIZE, (p.y + 1) * SQUARE_SIZE));
    }

    /**
     * Converts graphics coordinates to board coordinates ((0,0) to (8,9)).
     *
     * @param p the point to convert (in graphics coordinates)
     * @return the converted point (in board coordinates)
     */
    public static Point toBoardCoords(Point p) {
        int offset = SQUARE_SIZE / 2; // offset ensures rounding is accurate
        return (new Point((p.x + offset) / SQUARE_SIZE - 1, (p.y + offset) / SQUARE_SIZE - 1));
    }

    /**
     * Determine if the point is within the playing field.
     *
     * @param p the point to check (in board coordinates)
     * @return true if the point is in the field, false if the point is not in the
     *         field
     */
    public static boolean inBounds(Point p) {
        return ((p.x >= 0) && (p.x <= 8) && (p.y >= 0) && (p.y <= 9));
    }

    /**
     * Determine if the point is within a palace.
     *
     * @param p the point to check (in board coordinates)
     * @return true if the point is in the palace, false if the point is not
     */
    public static boolean inPalace(Point p) {
        return (((p.x >= 3) && (p.x <= 5) && (p.y >= 0) && (p.y <= 2)) || // top palace
                ((p.x >= 3) && (p.x <= 5) && (p.y >= 7) && (p.y <= 9)) // bottom palace
            );
    }

    /**
     * Determine if the point is within the red or the black field.
     *
     * @param p the point to check (in board coordinates)
     * @return red if the point is in the bottom half, black if not
     */
    public static Color fieldColor(Point p) {
        if ((p.x >= 0) && (p.x <= 8) && (p.y > 4) && (p.y <= 9)) { // coordinates for bottom half
            return Color.red;
        } else {
            return Color.black;
        }
    }

    /**
     * Change the status of the game to be accurate to the game board.
     */
    private void updateStatus() {
        GameState gs = cc.getGameState();
        switch (gs) {
            case BLACK_CHECKMATE:
                status.setText("Checkmate! Red Wins!");
                break;
            case RED_CHECKMATE:
                status.setText("Checkmate! Black Wins!");
                break;
            case BLACK_CHECK:
                status.setText("Check! Black turn. Select a piece.");
                break;
            case RED_CHECK:
                status.setText("Check! Red turn. Select a piece.");
                break;
            case BEGIN_MOVE_MODE:
                status.setText("Piece selected. Make a move.");
                break;
            case END_MOVE_MODE1: {
                if (cc.isRedTurn()) {
                    status.setText("Red turn. Select a piece.");
                } else {
                    status.setText("Black turn. Select a piece.");
                }
                break;
            }
            case END_MOVE_MODE2: {
                if (cc.isRedTurn()) {
                    status.setText("Invalid move... Red turn. Select a piece.");
                } else {
                    status.setText("Invalid move... Black turn. Select a piece.");
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * Paint the game board.
     * 
     * @param g the graphics context for the board
     */
    public void paintComponent(Graphics g) {
        g.setColor(new Color(199, 164, 87));
        g.fillRect(
                0, 0,
                BOARD_WIDTH, BOARD_HEIGHT
        );

        g.setColor(new Color(133, 109, 56, 182));
        g.setFont(new Font("Serif", Font.BOLD, BOARD_WIDTH / 3));

        // calculate string offsets in x and y to center the text
        int xOffset = g.getFontMetrics().stringWidth("象棋");
        int yOffset = g.getFontMetrics().getAscent();

        g.drawString("象棋", (BOARD_WIDTH / 2) - (xOffset / 2), (BOARD_HEIGHT / 2) + (yOffset / 2));

        paintBorders(g);
        paintGrid(g);
        paintGridDetails(g);
        paintPieces(g);
    }

    /**
     * Draw wide borders to hold the main board.
     * 
     * @param g the graphics context for the board
     */
    private void paintBorders(Graphics g) {
        // * draw wide borders around the main grid *//
        g.setColor(Color.black);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        int borderSize = SQUARE_SIZE - SQUARE_SIZE / 10;
        g2.drawLine(
                borderSize, borderSize,
                borderSize, BOARD_HEIGHT - borderSize
        );
        g2.drawLine(
                borderSize, borderSize,
                BOARD_WIDTH - borderSize, borderSize
        );
        g2.drawLine(
                BOARD_WIDTH - borderSize, borderSize,
                BOARD_WIDTH - borderSize, BOARD_HEIGHT - borderSize
        );
        g2.drawLine(
                borderSize, BOARD_HEIGHT - borderSize,
                BOARD_WIDTH - borderSize, BOARD_HEIGHT - borderSize
        );

        g2.setStroke(new BasicStroke(1));
    }

    /**
     * Draw main grid.
     * 
     * @param g the graphics context for the board
     */
    private void paintGrid(Graphics g) {
        // * draw horizontal lines, iterating through each column *//
        g.setColor(Color.black);

        for (int j = 0; j < 10; j++) {
            Point start1 = toGCoords(new Point(0, j));
            Point end1 = toGCoords(new Point(8, j));

            g.drawLine(start1.x, start1.y, end1.x, end1.y);
        }

        // * draw side borders (vertical lines) *//
        g.drawLine(
                SQUARE_SIZE, SQUARE_SIZE,
                SQUARE_SIZE, BOARD_HEIGHT - SQUARE_SIZE
        );
        g.drawLine(
                BOARD_WIDTH - SQUARE_SIZE, SQUARE_SIZE,
                BOARD_WIDTH - SQUARE_SIZE, BOARD_HEIGHT - SQUARE_SIZE
        );

        // * draw vertical lines, iterating through each row *//
        for (int i = 1; i < 9; i++) {
            Point start1 = toGCoords(new Point(i, 0));
            Point end1 = toGCoords(new Point(i, 4));

            Point start2 = toGCoords(new Point(i, 5));
            Point end2 = toGCoords(new Point(i, 9));

            g.drawLine(start1.x, start1.y, end1.x, end1.y);
            g.drawLine(start2.x, start2.y, end2.x, end2.y);

        }
    }

    /**
     * Draw inner grid details.
     * 
     * @param g the graphics context for the board
     */
    private void paintGridDetails(Graphics g) {
        // * Draw king's palace on each side *//
        g.setColor(Color.black);

        Point start1 = toGCoords(new Point(3, 0));
        Point end1 = toGCoords(new Point(5, 2));

        Point start2 = toGCoords(new Point(5, 0));
        Point end2 = toGCoords(new Point(3, 2));

        Point start3 = toGCoords(new Point(3, 7));
        Point end3 = toGCoords(new Point(5, 9));

        Point start4 = toGCoords(new Point(5, 7));
        Point end4 = toGCoords(new Point(3, 9));

        g.drawLine(start1.x, start1.y, end1.x, end1.y);
        g.drawLine(start2.x, start2.y, end2.x, end2.y);
        g.drawLine(start3.x, start3.y, end3.x, end3.y);
        g.drawLine(start4.x, start4.y, end4.x, end4.y);

    }

    /**
     * Draw pieces.
     * 
     * @param g the graphics context for the board
     */
    private void paintPieces(Graphics g) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if (cc.getBoard()[i][j] != null) {
                    cc.getBoard()[i][j].draw(g);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}