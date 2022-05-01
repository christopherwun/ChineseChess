package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Cannon is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. The red Cannon is labeled 炮,
 * while the
 * black Cannon is labeled 砲.
 *
 * Motion/Capture Rules: Their motion is in horizontal and vertical lines until
 * collision
 * with another piece. They can only capture pieces in horizontal/vertical
 * directions after
 * jumping over another piece. They cannot jump over pieces without capturing
 * and cannot
 * capture without jumping over pieces.
 *
 */
public class Cannon extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Cannon
     * @param c        the color of the Cannon
     * @param selected whether the piece is selected
     */
    public Cannon(Point p, Color c, boolean selected) {
        super(p, c, selected);
        if (c.equals(Color.red)) {
            this.setText("炮");
        } else {
            this.setText("砲");
        }
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Cannon according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Cannon can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();
        Set<Point> moves = new HashSet<>();

        // add moves to the right until collision with the nearest piece.
        int xCurr = p.x + 1;
        while ((xCurr <= 8) && (pieceArr[xCurr][p.y] == null)) {
            moves.add(new Point(xCurr, p.y));
            xCurr++;
        }
        // continue until the second collision, then add
        xCurr++;
        while (xCurr <= 8) {
            if (pieceArr[xCurr][p.y] != null) {
                moves.add(new Point(xCurr, p.y));
                break;
            }
            xCurr++;
        }

        // add moves to the left until collision with the nearest piece.
        xCurr = p.x - 1;
        while ((xCurr >= 0) && (pieceArr[xCurr][p.y] == null)) {
            moves.add(new Point(xCurr, p.y));
            xCurr--;
        }
        // continue adding until the second collision
        xCurr--;
        while (xCurr >= 0) {
            if (pieceArr[xCurr][p.y] != null) {
                moves.add(new Point(xCurr, p.y));
                break;
            }
            xCurr--;
        }

        // add moves downwards until collision with the nearest piece.
        int yCurr = p.y + 1;
        while ((yCurr <= 9) && (pieceArr[p.x][yCurr] == null)) {
            moves.add(new Point(p.x, yCurr));
            yCurr++;
        }
        // continue adding until the second collision
        yCurr++;
        while (yCurr <= 9) {
            if (pieceArr[p.x][yCurr] != null) {
                moves.add(new Point(p.x, yCurr));
                break;
            }
            yCurr++;
        }

        // add moves upwards until collision with the nearest piece.
        yCurr = p.y - 1;
        while ((yCurr >= 0) && (pieceArr[p.x][yCurr] == null)) {
            moves.add(new Point(p.x, yCurr));
            yCurr--;
        }
        // continue adding until the second collision
        yCurr--;
        while (yCurr >= 0) {
            if (pieceArr[p.x][yCurr] != null) {
                moves.add(new Point(p.x, yCurr));
                break;
            }
            yCurr--;
        }
        // add this piece's position (for capture)

        return moves;
    }

    /**
     * @return a copy of the original Cannon
     */
    public Cannon copy() {
        return new Cannon(this.getPosition(), this.getColor(), this.getIsSelected());
    }

}
