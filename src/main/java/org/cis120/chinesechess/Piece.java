package org.cis120.chinesechess;

import java.awt.*;
import java.util.*;

import static org.cis120.chinesechess.ChessBoard.*;

/**
 * A movable piece in the game.
 *
 * Game pieces exist in both the game's piece set and board. They have a subtype
 * of
 * Soldier, Cannon, Chariot, Horse, Elephant, Guard, or General based on their
 * possible
 * move types.
 *
 * They each have a position, selection status (selected or not), color, and
 * text. Their position determines their possible moves and the board status;
 * selection,
 * color, and text determine their appearance on the board.
 *
 * The position should always be in the bounds of the board and in array
 * coordinates. The
 * color should either be red or black. The text is determined in each subclass
 * depending
 * on the color and subtype.
 *
 */
public abstract class Piece {

    private Point position; // location of piece in board coordinates (piece is centered at this
                            // point)

    private boolean isSelected; // whether the piece is being moved or not
    private final Color color; // color of piece (immutable)
    private String text; // text on piece

    /**
     * Constructor
     */
    public Piece(Point p, Color c, boolean selected) {
        this.position = p; // set starting position
        this.isSelected = selected; // all piece start inactive
        this.color = c; // give piece a final color
        this.text = ""; // filler text to be changed in subtype constructor
    }

    // **************************************************************************
    // * GETTERS
    // **************************************************************************
    public Point getPosition() {
        return new Point(this.position.x, this.position.y);
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public Color getColor() {
        return new Color(this.color.getRGB());
    }

    public String getText() {
        return this.text;
    }

    // **************************************************************************
    // * SETTERS
    // **************************************************************************
    public void setPosition(Point position) {
        this.position = position;
    }

    public void select() {
        this.isSelected = true;
    }

    public void deselect() {
        this.isSelected = false;
    }

    public void setText(String s) {
        this.text = s;
    }

    // **************************************************************************
    // * OTHER METHODS (Cloning + Move-setting + Painting)
    // **************************************************************************

    /**
     * Copy method which is overwritten in all subclasses.
     * 
     * @return a copy of the piece
     */
    public Piece copy() {
        return null; // to be overwritten
    }

    /**
     * Method to find all possible moves, given the board layout. Overwritten for
     * each subclass.
     *
     * Note: Includes off-board moves, self-capture moves, etc. (depending on the
     * piece type)
     * which will be filtered upon use.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the piece
     *         can move to according to its movement patterns.
     */
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        return new HashSet<>(); // this method will be overwritten for each subclass
    }

    /**
     * Method to draw the piece at its position based on its selection mode, color,
     * and text.
     *
     * Note: Text is determined by the subclass, so subclasses are differentiated by
     * text.
     *
     * @param g The graphics context of the game
     */
    public void draw(Graphics g) {

        Point p = toGCoords(this.position); // convert the array coords to graphics coords first

        // paint a selection circle to show the player which piece they are moving
        if (this.isSelected) {
            g.setColor(new Color(194, 191, 35, 232));
            int d0 = SQUARE_SIZE + (SQUARE_SIZE / 25);
            g.fillOval(p.x - (d0 / 2), p.y - (d0 / 2), d0, d0);
        }

        // draw piece
        drawPiece(p, g);
        drawText(p, g);
    }

    /**
     * Helper method for draw: paint general piece at a point with a given graphics
     * context
     *
     * @param g The graphics context of the game
     * @param p The graphics coordinates of the piece
     */
    private void drawPiece(Point p, Graphics g) {

        // paint piece in three circles for shading
        g.setColor(new Color(133, 97, 35, 232));
        int d1 = (SQUARE_SIZE) - (SQUARE_SIZE / 50);
        g.fillOval(p.x - (d1 / 2), p.y - (d1 / 2), d1, d1); // outer circle

        g.setColor(new Color(182, 133, 51, 232));
        int d2 = (SQUARE_SIZE) - (SQUARE_SIZE / 50) - (SQUARE_SIZE / 10);
        g.fillOval(p.x - (d2 / 2), p.y - (d2 / 2), d2, d2); // middle circle

        g.setColor(new Color(198, 143, 48, 247));
        int d3 = (SQUARE_SIZE) - (SQUARE_SIZE / 50) - (SQUARE_SIZE / 5);
        g.fillOval(p.x - (d3 / 2), p.y - (d3 / 2), d3, d3); // inner circle
    }

    /**
     * Helper method for draw: paint text at a point with a given graphics context
     *
     * @param g The graphics context of the game
     * @param p The graphics coordinates of the piece
     */
    private void drawText(Point p, Graphics g) {

        int fontSize = BOARD_WIDTH / 20; // find font size based on the board size

        g.setColor(this.color); // set color to the color of the piece
        g.setFont(new Font("Serif", Font.BOLD, fontSize));

        // calculate string offsets in x and y to center the text
        int xOffset = g.getFontMetrics().stringWidth(this.text);
        int yOffset = g.getFontMetrics().getAscent();

        g.drawString(this.text, p.x - (xOffset / 2), p.y + (yOffset / 2));
    }

}
