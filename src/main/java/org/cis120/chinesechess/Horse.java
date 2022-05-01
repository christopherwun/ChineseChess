package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Horse is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. Both the red and black Horse are
 * labeled
 * as 馬.
 *
 * Motion/Capture Rules: Their motion is one step either horizontally or
 * vertically in
 * any direction, then a diagonal step continuing in that direction (one
 * horizontal, one
 * vertical). They may not capture or move through pieces on their first step.
 * They can
 * capture pieces they collide with on their second step.
 *
 */
public class Horse extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Horse
     * @param c        the color of the Horse
     * @param selected whether the piece is selected
     */
    public Horse(Point p, Color c, boolean selected) {
        super(p, c, selected);
        this.setText("馬");
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Horse according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Horse can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();

        Set<Point> moves = new HashSet<>();

        // add rightward moves, but only if no piece blocks the first part of the move
        if ((p.x + 1 <= 8) && (pieceArr[p.x + 1][p.y] == null)) {
            moves.add(new Point(p.x + 2, p.y + 1));
            moves.add(new Point(p.x + 2, p.y - 1));
        }

        // add leftward moves, but only if no piece blocks the first part of the move
        if ((p.x - 1 >= 0) && (pieceArr[p.x - 1][p.y] == null)) {
            moves.add(new Point(p.x - 2, p.y + 1));
            moves.add(new Point(p.x - 2, p.y - 1));
        }

        // add downward moves, but only if no piece blocks the first part of the move
        if ((p.y + 1 <= 9) && (pieceArr[p.x][p.y + 1] == null)) {
            moves.add(new Point(p.x + 1, p.y + 2));
            moves.add(new Point(p.x - 1, p.y + 2));
        }

        // add upward moves, but only if no piece blocks the first part of the move
        if ((p.y - 1 >= 0) && (pieceArr[p.x][p.y - 1] == null)) {
            moves.add(new Point(p.x + 1, p.y - 2));
            moves.add(new Point(p.x - 1, p.y - 2));
        }

        return moves;
    }

    /**
     * @return a copy of the original Horse
     */
    @Override
    public Horse copy() {
        return new Horse(this.getPosition(), this.getColor(), this.getIsSelected());
    }

}
