package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Chariot is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. Both the red and black Chariot
 * are labeled
 * as 車.
 *
 * Motion/Capture Rules: Their motion is in horizontal and vertical lines until
 * collision
 * with another piece. They can capture the first piece they collide with. They
 * cannot
 * jump over other pieces to move or capture.
 *
 */
public class Chariot extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Chariot
     * @param c        the color of the Chariot
     * @param selected whether the piece is selected
     */
    public Chariot(Point p, Color c, boolean selected) {
        super(p, c, selected);
        this.setText("車");
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Chariot according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Chariot can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();

        Set<Point> moves = new HashSet<>();

        // add moves to the right until collision with the nearest piece.
        int xCurr = p.x + 1;
        while (xCurr <= 8) {
            moves.add(new Point(xCurr, p.y));
            if (pieceArr[xCurr][p.y] != null) {
                break;
            }
            xCurr++;
        }

        // add moves to the left until collision with the nearest piece.
        xCurr = p.x - 1;
        while (xCurr >= 0) {
            moves.add(new Point(xCurr, p.y));
            if (pieceArr[xCurr][p.y] != null) {
                break;
            }
            xCurr--;
        }

        // add moves downwards until collision with the nearest piece.
        int yCurr = p.y + 1;
        while (yCurr <= 9) {
            moves.add(new Point(p.x, yCurr));
            if (pieceArr[p.x][yCurr] != null) {
                break;
            }
            yCurr++;
        }

        // add moves upwards until collision with the nearest piece.
        yCurr = p.y - 1;
        while (yCurr >= 0) {
            moves.add(new Point(p.x, yCurr));
            if (pieceArr[p.x][yCurr] != null) {
                break;
            }
            yCurr--;
        }

        return moves;
    }

    /**
     * @return a copy of the original Chariot
     */
    @Override
    public Chariot copy() {
        return new Chariot(this.getPosition(), this.getColor(), this.getIsSelected());
    }
}
