package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Soldier is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. The red Soldier is labeled 兵,
 * while the
 * black Soldier is labeled 卒.
 *
 * Motion/Capture Rules: Their motion and capture is in single steps forward in
 * the start
 * of the game. After crossing the river (midline of the board), they can move
 * and capture
 * to the left and right as well.
 *
 */
public class Soldier extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Soldier
     * @param c        the color of the Soldier
     * @param selected whether the piece is selected
     */
    public Soldier(Point p, Color c, boolean selected) {
        super(p, c, selected);
        if (c.equals(Color.red)) {
            this.setText("兵");
        } else {
            this.setText("卒");
        }
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Soldier according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Soldier can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();
        Color c = this.getColor();

        Set<Point> moves = new HashSet<>();

        // Soldiers can always move forwards (although forwards is diff for each color)
        if (c.equals(Color.red)) {
            moves.add(new Point(p.x, p.y - 1));
        } else {
            moves.add(new Point(p.x, p.y + 1));
        }

        // Soldiers can horizontally after crossing the river
        if (!(ChessBoard.fieldColor(p).equals(c))) {
            moves.add(new Point(p.x + 1, p.y));
            moves.add(new Point(p.x - 1, p.y));
        }

        return moves;
    }

    /**
     * @return a copy of the original Soldier
     */
    @Override
    public Soldier copy() {
        return new Soldier(this.getPosition(), this.getColor(), this.getIsSelected());
    }

}
