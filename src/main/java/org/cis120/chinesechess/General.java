package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * General is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. The red General is labeled 帥,
 * while the
 * black General is labeled 將.
 *
 * Motion/Capture Rules: They can move and capture one step horizontally or
 * vertically in
 * any direction. They cannot leave their palace (middle square on their side of
 * the field).
 *
 */
public class General extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the General
     * @param c        the color of the General
     * @param selected whether the piece is selected
     */
    public General(Point p, Color c, boolean selected) {
        super(p, c, selected);
        if (c.equals(Color.red)) {
            this.setText("帥");
        } else {
            this.setText("將");
        }
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * General according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         General can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();
        Color c = this.getColor();

        Set<Point> moves = new HashSet<>();
        Set<Point> validMoves = new HashSet<>();

        // * add possible moves, 1 space vertically or 1 space horizontally *//
        moves.add(new Point(p.x + 1, p.y));
        moves.add(new Point(p.x - 1, p.y));
        moves.add(new Point(p.x, p.y + 1));
        moves.add(new Point(p.x, p.y - 1));

        // * clean moves (must be in the palace this time) *//
        for (Point move : moves) {
            if ((ChessBoard.inPalace(move)) && ChessBoard.fieldColor(move).equals(c)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * @return a copy of the original General
     */
    @Override
    public General copy() {
        return new General(this.getPosition(), this.getColor(), this.getIsSelected());
    }
}
