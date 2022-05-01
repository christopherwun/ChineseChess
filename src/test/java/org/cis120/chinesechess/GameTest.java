package org.cis120.chinesechess;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains JUnit Testing for ChineseChess,
 * ChessBoard, and Piece subtypes.
 *
 */
public class GameTest {

    /**
     * A basic testing array that can be instantiated for simple board setup
     * testing.
     * Cannot be used for advanced chess features (not the same as ChessBoard). Can
     * only store an array of pieces to test positioning.
     *
     */
    private static class TestBoard {
        private final Piece[][] board; // stored array

        /**
         * Constructor
         */
        public TestBoard() {
            board = new Piece[9][10];
        }

        /**
         * Add pieces to the board.
         *
         * @param piece the piece to be added
         */
        public void add(Piece piece) {
            Point p = piece.getPosition();
            board[p.x][p.y] = piece;
        }

        /**
         * @return the object's board
         */
        public Piece[][] array() {
            return this.board;
        }
    }

    // **************************************************************************
    // * PIECE TESTS
    // **************************************************************************

    // SOLDIERS
    @Test
    public void testSoldierConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Soldier(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("兵", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testSoldierPreRiverMotion() {
        Point p = new Point(3, 8);
        Color c = Color.red;

        Piece piece = new Soldier(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(1, ms.size());
        assertTrue(ms.contains(new Point(3, 7)));
    }

    @Test
    public void testSoldierPostRiverMotion() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Soldier(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(3, ms.size());
        assertTrue(ms.contains(new Point(3, 9)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
    }

    // CANNONS
    @Test
    public void testCannonConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Cannon(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("炮", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testCannonMotionNoInterference() {
        Point p = new Point(3, 8);
        Color c = Color.red;

        Piece piece = new Cannon(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(17, ms.size());

        // test vertical moves
        assertTrue(ms.contains(new Point(3, 0)));
        assertTrue(ms.contains(new Point(3, 1)));
        assertTrue(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(3, 3)));
        assertTrue(ms.contains(new Point(3, 4)));
        assertTrue(ms.contains(new Point(3, 5)));
        assertTrue(ms.contains(new Point(3, 6)));
        assertTrue(ms.contains(new Point(3, 7)));
        assertTrue(ms.contains(new Point(3, 9)));

        // test horizontal moves
        assertTrue(ms.contains(new Point(0, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(6, 8)));
        assertTrue(ms.contains(new Point(7, 8)));
        assertTrue(ms.contains(new Point(8, 8)));
    }

    @Test
    public void testCannonMotionWithInterference() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Cannon(p, c, false); // construct piece
        TestBoard tb = new TestBoard();
        tb.add(piece);

        Piece blocker = new Soldier(new Point(3, 2), c, false);
        tb.add(blocker);

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());

        assertEquals(14, ms.size());

        // test vertical moves
        assertFalse(ms.contains(new Point(3, 0)));
        assertFalse(ms.contains(new Point(3, 1)));
        assertFalse(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(3, 3)));
        assertTrue(ms.contains(new Point(3, 4)));
        assertTrue(ms.contains(new Point(3, 5)));
        assertTrue(ms.contains(new Point(3, 6)));
        assertTrue(ms.contains(new Point(3, 7)));
        assertTrue(ms.contains(new Point(3, 9)));

        // test horizontal moves
        assertTrue(ms.contains(new Point(0, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(6, 8)));
        assertTrue(ms.contains(new Point(7, 8)));
        assertTrue(ms.contains(new Point(8, 8)));
    }

    @Test
    public void testCannonMotionCapture() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Cannon(p, c, false); // construct piece
        TestBoard tb = new TestBoard();
        tb.add(piece);

        Piece blocker = new Soldier(new Point(3, 2), c, false);
        tb.add(blocker);

        Piece target = new Soldier(new Point(3, 0), c, false);
        tb.add(target);

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(15, ms.size());

        // test target
        assertTrue(ms.contains(new Point(3, 0)));

        // test vertical moves
        assertFalse(ms.contains(new Point(3, 1)));
        assertFalse(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(3, 3)));
        assertTrue(ms.contains(new Point(3, 4)));
        assertTrue(ms.contains(new Point(3, 5)));
        assertTrue(ms.contains(new Point(3, 6)));
        assertTrue(ms.contains(new Point(3, 7)));
        assertTrue(ms.contains(new Point(3, 9)));

        // test horizontal moves
        assertTrue(ms.contains(new Point(0, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(6, 8)));
        assertTrue(ms.contains(new Point(7, 8)));
        assertTrue(ms.contains(new Point(8, 8)));
    }

    // chariots
    @Test
    public void testChariotConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Chariot(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("車", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testChariotMotionNoInterference() {
        Point p = new Point(3, 8);
        Color c = Color.red;

        Piece piece = new Chariot(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(17, ms.size());

        // test vertical moves
        assertTrue(ms.contains(new Point(3, 0)));
        assertTrue(ms.contains(new Point(3, 1)));
        assertTrue(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(3, 3)));
        assertTrue(ms.contains(new Point(3, 4)));
        assertTrue(ms.contains(new Point(3, 5)));
        assertTrue(ms.contains(new Point(3, 6)));
        assertTrue(ms.contains(new Point(3, 7)));
        assertTrue(ms.contains(new Point(3, 9)));

        // test horizontal moves
        assertTrue(ms.contains(new Point(0, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(6, 8)));
        assertTrue(ms.contains(new Point(7, 8)));
        assertTrue(ms.contains(new Point(8, 8)));
    }

    @Test
    public void testChariotMotionWithInterference() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Chariot(p, c, false); // construct piece
        TestBoard tb = new TestBoard();
        tb.add(piece);

        Piece blocker = new Soldier(new Point(3, 2), c, false);
        tb.add(blocker);

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());

        assertEquals(15, ms.size());

        // test vertical moves
        assertFalse(ms.contains(new Point(3, 0)));
        assertFalse(ms.contains(new Point(3, 1)));
        assertTrue(ms.contains(new Point(3, 2))); // can capture blocker
        assertTrue(ms.contains(new Point(3, 3)));
        assertTrue(ms.contains(new Point(3, 4)));
        assertTrue(ms.contains(new Point(3, 5)));
        assertTrue(ms.contains(new Point(3, 6)));
        assertTrue(ms.contains(new Point(3, 7)));
        assertTrue(ms.contains(new Point(3, 9)));

        // test horizontal moves
        assertTrue(ms.contains(new Point(0, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
        assertTrue(ms.contains(new Point(2, 8)));
        assertTrue(ms.contains(new Point(4, 8)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(6, 8)));
        assertTrue(ms.contains(new Point(7, 8)));
        assertTrue(ms.contains(new Point(8, 8)));
    }

    // horses
    @Test
    public void testHorseConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Horse(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("馬", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testHorseMotionNoInterference() {
        Point p = new Point(3, 8);
        Color c = Color.red;

        Piece piece = new Horse(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(8, ms.size());

        assertTrue(ms.contains(new Point(4, 10)));
        assertTrue(ms.contains(new Point(4, 6)));
        assertTrue(ms.contains(new Point(2, 10)));
        assertTrue(ms.contains(new Point(2, 6)));
        assertTrue(ms.contains(new Point(5, 9)));
        assertTrue(ms.contains(new Point(1, 9)));
        assertTrue(ms.contains(new Point(5, 7)));
        assertTrue(ms.contains(new Point(1, 7)));
    }

    @Test
    public void testHorseMotionWithInterference() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Horse(p, c, false); // construct piece
        TestBoard tb = new TestBoard();
        tb.add(piece);

        tb.add(new Soldier(new Point(3, 7), c, false));
        tb.add(new Soldier(new Point(3, 9), c, false));

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());

        assertEquals(4, ms.size());

        assertFalse(ms.contains(new Point(4, 10)));
        assertFalse(ms.contains(new Point(4, 6)));
        assertFalse(ms.contains(new Point(2, 10)));
        assertFalse(ms.contains(new Point(2, 6)));
        assertTrue(ms.contains(new Point(5, 9)));
        assertTrue(ms.contains(new Point(1, 9)));
        assertTrue(ms.contains(new Point(5, 7)));
        assertTrue(ms.contains(new Point(1, 7)));
    }

    @Test
    public void testHorseMotionEdge() {
        Point p = new Point(3, 9);
        Color c = Color.red;

        Piece piece = new Horse(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(6, ms.size());

        assertFalse(ms.contains(new Point(4, 11)));
        assertTrue(ms.contains(new Point(4, 7)));
        assertFalse(ms.contains(new Point(2, 11)));
        assertTrue(ms.contains(new Point(2, 7)));
        assertTrue(ms.contains(new Point(5, 10)));
        assertTrue(ms.contains(new Point(1, 10)));
        assertTrue(ms.contains(new Point(5, 8)));
        assertTrue(ms.contains(new Point(1, 8)));
    }

    // elephants
    @Test
    public void testElephantConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Elephant(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("相", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testElephantMotionNoInterference() {
        Point p = new Point(3, 8);
        Color c = Color.red;

        Piece piece = new Elephant(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(4, ms.size());

        assertTrue(ms.contains(new Point(5, 10)));
        assertTrue(ms.contains(new Point(1, 10)));
        assertTrue(ms.contains(new Point(5, 6)));
        assertTrue(ms.contains(new Point(1, 6)));
    }

    @Test
    public void testElephantMotionWithInterference() {
        Point p = new Point(3, 8);
        Color c = Color.black;

        Piece piece = new Elephant(p, c, false); // construct piece
        TestBoard tb = new TestBoard();
        tb.add(piece);

        tb.add(new Soldier(new Point(4, 7), c, false));
        tb.add(new Soldier(new Point(2, 9), c, false));

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());

        assertEquals(2, ms.size());

        assertTrue(ms.contains(new Point(5, 10)));
        assertFalse(ms.contains(new Point(1, 10)));
        assertFalse(ms.contains(new Point(5, 6)));
        assertTrue(ms.contains(new Point(1, 6)));
    }

    @Test
    public void testElephantsMotionEdge() {
        Point p = new Point(3, 9);
        Color c = Color.red;

        Piece piece = new Elephant(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(2, ms.size());

        assertFalse(ms.contains(new Point(5, 11)));
        assertFalse(ms.contains(new Point(1, 11)));
        assertTrue(ms.contains(new Point(5, 7)));
        assertTrue(ms.contains(new Point(1, 7)));
    }

    // guards
    @Test
    public void testGuardConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new Guard(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("士", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testGuardMotionInPalace() {
        Point p = new Point(4, 1);
        Color c = Color.black;

        Piece piece = new Guard(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(4, ms.size());

        assertTrue(ms.contains(new Point(5, 2)));
        assertTrue(ms.contains(new Point(5, 0)));
        assertTrue(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(3, 0)));
    }

    @Test
    public void testGuardMotionWrongPalace() {
        Point p = new Point(4, 1);
        Color c = Color.red;

        Piece piece = new Guard(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(0, ms.size());
    }

    @Test
    public void testGuardMotionPalaceCorner() {
        Point p = new Point(3, 2);
        Color c = Color.black;

        Piece piece = new Guard(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(1, ms.size());

        assertTrue(ms.contains(new Point(4, 1)));
        assertFalse(ms.contains(new Point(4, 3)));
        assertFalse(ms.contains(new Point(2, 1)));
        assertFalse(ms.contains(new Point(2, 3)));
    }

    @Test
    public void testGuardMotionPalaceEdge() {
        Point p = new Point(3, 1);
        Color c = Color.black;

        Piece piece = new Guard(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(2, ms.size());

        assertTrue(ms.contains(new Point(4, 0)));
        assertTrue(ms.contains(new Point(4, 2)));
        assertFalse(ms.contains(new Point(2, 0)));
        assertFalse(ms.contains(new Point(2, 2)));
    }

    // generals
    @Test
    public void testGeneralConstructor() {
        Point p = new Point(0, 0);
        Color c = Color.red;

        Piece piece = new General(p, c, false); // construct piece

        // test instance variables position, color, text, selection
        assertEquals(new Point(0, 0), piece.getPosition());
        assertEquals(Color.red, piece.getColor());
        assertEquals("帥", piece.getText());
        assertFalse(piece.getIsSelected());
    }

    @Test
    public void testGeneralMotionInPalace() {
        Point p = new Point(4, 1);
        Color c = Color.black;

        Piece piece = new General(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(4, ms.size());

        assertTrue(ms.contains(new Point(5, 1)));
        assertTrue(ms.contains(new Point(3, 1)));
        assertTrue(ms.contains(new Point(4, 2)));
        assertTrue(ms.contains(new Point(4, 0)));
    }

    @Test
    public void testGeneralMotionWrongPalace() {
        Point p = new Point(4, 1);
        Color c = Color.red;

        Piece piece = new General(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(0, ms.size());
    }

    @Test
    public void testGeneralMotionPalaceCorner() {
        Point p = new Point(3, 2);
        Color c = Color.black;

        Piece piece = new General(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(2, ms.size());

        assertTrue(ms.contains(new Point(4, 2)));
        assertTrue(ms.contains(new Point(3, 1)));
        assertFalse(ms.contains(new Point(3, 3)));
        assertFalse(ms.contains(new Point(2, 2)));
    }

    @Test
    public void testGeneralMotionPalaceEdge() {
        Point p = new Point(3, 1);
        Color c = Color.black;

        Piece piece = new General(p, c, false); // construct piece
        TestBoard tb = new TestBoard();

        // test move set
        Set<Point> ms = piece.getMoveSet(tb.array());
        assertEquals(3, ms.size());

        assertTrue(ms.contains(new Point(3, 0)));
        assertTrue(ms.contains(new Point(3, 2)));
        assertTrue(ms.contains(new Point(4, 1)));
        assertFalse(ms.contains(new Point(2, 1)));
    }

    // **************************************************************************
    // * BOARD TESTS
    // **************************************************************************

    @Test
    public void testChessBoardInitializer() {

    }

}
