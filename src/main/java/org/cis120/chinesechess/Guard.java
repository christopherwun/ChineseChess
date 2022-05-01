package org.cis120.chinesechess;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;

/**
 * A type of movable piece in the game.
 *
 * Guard is a subtype of Piece, so it also stores a position in board array
 * coordinates,
 * selection status, immutable color and text. The red Guard is labeled 士, while
 * the
 * black Guard is labeled 仕.
 *
 * Motion/Capture Rules: They can move and capture one step diagonally in any
 * direction
 * (one horizontal + one vertical in any combination). They cannot leave their
 * general's
 * palace (middle square on their side of the field).
 *
 */
public class Guard extends Piece {

    /**
     * Constructor
     *
     * @param p        the starting point of the Guard
     * @param c        the color of the Guard
     * @param selected whether the piece is selected
     */
    public Guard(Point p, Color c, boolean selected) {
        super(p, c, selected);
        if (c.equals(Color.red)) {
            this.setText("士");
        } else {
            this.setText("仕");
        }
    }

    /**
     * Override getMoveSet() from Piece class to create a set of possible moves for
     * a
     * Guard according to the specific motion/capture rules above.
     *
     * @param pieceArr The board layout
     * @return A Set (HashSet) of points (in board array coordinates) that the
     *         Guard can move to according to its current position.
     */
    @Override
    public Set<Point> getMoveSet(Piece[][] pieceArr) {
        Point p = this.getPosition();
        Color c = this.getColor();

        Set<Point> moves = new HashSet<>();
        Set<Point> validMoves = new HashSet<>();

        // * add possible moves, 1 space vertically, 1 space horizontally *//
        moves.add(new Point(p.x + 1, p.y + 1));
        moves.add(new Point(p.x + 1, p.y - 1));
        moves.add(new Point(p.x - 1, p.y + 1));
        moves.add(new Point(p.x - 1, p.y - 1));

        // * clean moves (must be in the palace this time) *//
        for (Point move : moves) {
            if ((ChessBoard.inPalace(move)) && ChessBoard.fieldColor(move).equals(c)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * @return a copy of the original Guard
     */
    @Override
    public Guard copy() {
        return new Guard(this.getPosition(), this.getColor(), this.getIsSelected());
    }
}
