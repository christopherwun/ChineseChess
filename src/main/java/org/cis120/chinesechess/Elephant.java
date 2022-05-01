package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Elephant is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. The red Elephant is labeled 相,
 * while the
 * black Elephant is labeled 象.
 *
 * Motion/Capture Rules: Their motion and is two steps diagonally in any
 * direction (two
 * horizontal + two vertical in any combination). They cannot jump over pieces
 * in their way,
 * and can only capture pieces at the end of their diagonal movement.
 *
 */
public class Elephant extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Elephant
     * @param c        the color of the Elephant
     * @param selected whether the piece is selected
     */
    public Elephant(Point p, Color c, boolean selected) {
        super(p, c, selected);
        if (c.equals(Color.red)) {
            this.setText("相");
        } else {
            this.setText("象");
        }
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Elephant according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Elephant can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();

        Set<Point> moves = new HashSet<>();

        // add possible moves, 2 spaces vertically, 2 spaces horizontally
        if ((p.x + 1 <= 8) && (p.y + 1 <= 9) && (pieceArr[p.x + 1][p.y + 1] == null)) {
            moves.add(new Point(p.x + 2, p.y + 2));
        }
        if ((p.x + 1 <= 8) && (p.y - 1 >= 0) && (pieceArr[p.x + 1][p.y - 1] == null)) {
            moves.add(new Point(p.x + 2, p.y - 2));
        }
        if ((p.x - 1 >= 0) && (p.y + 1 <= 9) && (pieceArr[p.x - 1][p.y + 1] == null)) {
            moves.add(new Point(p.x - 2, p.y + 2));
        }
        if ((p.x - 1 >= 0) && (p.y - 1 >= 0) && (pieceArr[p.x - 1][p.y - 1] == null)) {
            moves.add(new Point(p.x - 2, p.y - 2));
        }

        return moves;
    }

    /**
     * @return a copy of the original Elephant
     */
    @Override
    public Elephant copy() {
        return new Elephant(this.getPosition(), this.getColor(), this.getIsSelected());
    }
}
