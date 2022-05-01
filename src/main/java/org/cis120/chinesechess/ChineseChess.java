package org.cis120.chinesechess;

import java.awt.*;
import java.util.*;

/**
 * This class is a model for Chinese Chess. It stores a board which contains
 * pieces,
 * the number of turns played, the player whose turn it is, the game state,
 * which piece
 * was last in play, and the generals of the game. See individual Piece
 * subclasses for
 * movement guidelines and ChessBoard class for controller and view.
 *
 * Note: This game uses the Model-View-Controller framework, so the model is
 * independent
 * of the visuals (which are controlled in ChessBoard.java). It can be JUnit
 * tested without
 * using the Java Swing library.
 *
 * The main method of this file plays a short game of Chinese Chess, which is
 * printed to the
 * console.
 * 
 */
public class ChineseChess {
    private Piece[][] board; // the game board
    private int numTurns; // the number of turns
    private boolean redTurn; // whether it is red's turn
    private GameState gameState; // the game state
    private Piece pieceInPlay; // the piece being moved
    private Map<Color, General> generals; // a map of the colors to their generals

    // **************************************************************************
    // * SETUP METHODS (Constructor + Reset + Helpers)
    // **************************************************************************
    /**
     * Constructor sets up game state via the reset function below.
     */
    public ChineseChess() {
        reset();
    }

    /**
     * Reset game state (start a new game)
     */
    public void reset() {
        board = new Piece[9][10]; // initializes possible points
        numTurns = 0;
        redTurn = true;
        gameState = GameState.END_MOVE_MODE1;
        pieceInPlay = null;
        generals = new HashMap<>();

        // * add all starting pieces to board *//
        placeStartingPieces(Color.red);
        placeStartingPieces(Color.black);
    }

    /**
     * A helper method for the reset() function, which adds all
     * pieces of one color to the board in their starting positions.
     */
    private void placeStartingPieces(Color c) {
        int sRow;
        int cRow;
        int r;

        // set starting rows based on the color
        if (c.equals(Color.red)) {
            sRow = 6;
            cRow = 7;
            r = 9;
        } else {
            sRow = 3;
            cRow = 2;
            r = 0;
        }

        // soldiers
        for (int i = 0; i < 5; i++) {
            this.board[2 * i][sRow] = new Soldier(new Point(2 * i, sRow), c, false);
        }

        // cannons
        this.board[1][cRow] = new Cannon(new Point(1, cRow), c, false);
        this.board[7][cRow] = new Cannon(new Point(7, cRow), c, false);

        // chariots
        this.board[0][r] = (new Chariot(new Point(0, r), c, false));
        this.board[8][r] = (new Chariot(new Point(8, r), c, false));

        // horses
        this.board[1][r] = (new Horse(new Point(1, r), c, false));
        this.board[7][r] = (new Horse(new Point(7, r), c, false));

        // elephants
        this.board[2][r] = (new Elephant(new Point(2, r), c, false));
        this.board[6][r] = (new Elephant(new Point(6, r), c, false));

        // guard
        this.board[3][r] = (new Guard(new Point(3, r), c, false));
        this.board[5][r] = (new Guard(new Point(5, r), c, false));

        // general
        General general = new General(new Point(4, r), c, false);
        this.board[4][r] = general;
        this.generals.put(c, general);
    }

    // **************************************************************************
    // * PLAYING TURNS
    // **************************************************************************
    /**
     * playMove processes a player's click. If the game is over, this function does
     * nothing.
     * If a piece is not yet selected, this click selects a piece. If a piece is
     * selected,
     * and the click is a legal next move, the move is made. If it is not legal, the
     * mode changes
     * to indicate a failed move attempt.
     *
     * @param p point of click
     */
    public void playMove(Point p) {

        // set the appropriate color based on whose turn it is
        Color c;
        if (this.isRedTurn()) {
            c = Color.red;
        } else {
            c = Color.black;
        }

        Piece piece = this.board[p.x][p.y]; // get the contents of the board at the location of the
                                            // click

        // pattern match based on the current game state to determine course of action
        switch (this.getGameState()) {

            // in these cases, a piece has not been selected yet
            case END_MOVE_MODE1:
            case END_MOVE_MODE2:
            case RED_CHECK:
            case BLACK_CHECK: {
                // proceed if the piece exists and is the same color as the current player
                if ((piece != null) && (c.equals(piece.getColor()))) {
                    piece.select();
                    setPieceInPlay(piece); // set selected piece
                    setGameState(GameState.BEGIN_MOVE_MODE); // switch game mode
                }
                break;
            }

            // in this case, a piece has been selected
            case BEGIN_MOVE_MODE: {
                // proceed if the move is a LEGAL move in the selected piece's move set
                if (((pieceInPlay.getMoveSet(this.getBoard()).contains(p)))
                        && (isLegalMove(pieceInPlay.copy(), p))) {

                    // change location of piece on board
                    Point oldPos = this.getPieceInPlay().getPosition();
                    board[oldPos.x][oldPos.y] = null;
                    board[p.x][p.y] = pieceInPlay;

                    pieceInPlay.setPosition(p); // change location of piece in the piece itself
                    gameState = GameState.END_MOVE_MODE1; // change game state
                    pieceInPlay.deselect(); // deselect piece
                    setRedTurn(!redTurn); // change turn
                    numTurns++; // increase number of turns

                    // after a successful move, check the new state of the game
                    checkState();

                } else {
                    // for an unsuccessful move, change the status to show illegal move and deselect
                    // piece
                    setGameState(GameState.END_MOVE_MODE2);
                    pieceInPlay.deselect();
                }
                break;
            }
            default:
                break;
        }
    }

    // **************************************************************************
    // * BOARD STATUS CHECKERS (Legal moves + Check + Checkmate + Status + Print)
    // **************************************************************************

    /**
     * Checks whether the move is legal. A move is legal if it is within the game
     * board,
     * does not move to a space occupied by the moving piece's own color, and does
     * not
     * leave the piece's general in check.
     *
     * @return true if the move is legal, false if the move is not
     */
    public boolean isLegalMove(Piece piece, Point p) {
        Color c = piece.getColor();

        // check that move is in bounds
        if ((ChessBoard.inBounds(p)) &&

        // check that move-to location is either null or opposite color
                (board[p.x][p.y] == null || !c.equals(board[p.x][p.y].getColor()))) {

            // try move to see if it causes check
            Point origCoords = pieceInPlay.getPosition();
            Piece origPiece = board[p.x][p.y];

            board[origCoords.x][origCoords.y] = null;
            board[p.x][p.y] = pieceInPlay;
            pieceInPlay.setPosition(p);

            boolean inCheck = inCheck(board, c);

            board[origCoords.x][origCoords.y] = pieceInPlay;
            board[p.x][p.y] = origPiece;
            pieceInPlay.setPosition(origCoords);

            // if not in check, the move is legal
            return !inCheck;

        } else { // out of bounds or finding a same-color piece at this point indicates illegal
                 // move
            return false;
        }
    }

    /**
     * Tests whether a particular color's general is in check (if it can be captured
     * by an enemy piece on the following turn).
     *
     * Note: A general is also in check if it is in line with its opposing general
     * and
     * there are no pieces blocking them from each other.
     *
     * @return true if the general is in check, false if it is not
     */
    public boolean inCheck(Piece[][] testBoard, Color c) {
        // get opponent's color
        Color oppC;
        if (c.equals(Color.red)) {
            oppC = Color.black;
        } else {
            oppC = Color.red;
        }

        // get both generals from map
        General gen = generals.get(c);
        General oppGen = generals.get(oppC);

        // get both positions from generals
        Point pos = gen.getPosition();
        Point oppPos = oppGen.getPosition();

        // if both generals are in the same column (same x-cor), check if there are
        // pieces between them to block
        if ((pos.x == oppPos.x) && c.equals(Color.red)) { // check red

            // iterate through pieces (start at the one 1 after the first general and end at
            // the other general)
            for (int j = pos.y - 1; j >= oppPos.y; j--) {

                // if the board spot is occupied, break the loop. if the other general is
                // reached, inCheck is true
                if (j == oppPos.y) {
                    return true;
                } else if (testBoard[pos.x][j] != null) {
                    break;
                }
            }

        } else if ((pos.x == oppPos.x) && c.equals(Color.black)) { // check black
            // iterate through pieces (start at the one 1 after the first general and end at
            // the other general)
            for (int j = pos.y + 1; j <= oppPos.y; j++) {

                // if the board spot is occupied, break the loop. if the other general is
                // reached, inCheck is true
                if (j == oppPos.y) {
                    return true;
                } else if (testBoard[pos.x][j] != null) {
                    break;
                }
            }
        }

        // iterate through board array's pieces to check if any piece on the board can
        // capture the general
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                Piece piece = testBoard[i][j]; // find piece from board
                if ((piece != null) && (!piece.getColor().equals(c))) {
                    if (piece.getMoveSet(testBoard).contains(gen.getPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Tests whether a particular color's general is in checkmate (if any moves can
     * be
     * made by their pieces.
     *
     * Note: Unlike international chess, a general is in checkmate if its team
     * cannot
     * make any legal moves, EVEN if the general is not in check.
     *
     * @return true if the general is in checkmate, false if it is not
     */
    public boolean inCheckmate(Color c) {
        // iterate through the board array's pieces
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                Piece piece = this.getPiece(i, j);

                // if piece is in the correct color, iterate through all possible moves
                if ((piece != null) && (piece.getColor().equals(c))) {

                    // if any possible move is legal, no change to state is necessary
                    for (Point move : piece.getMoveSet(this.getBoard())) {
                        if (isLegalMove(piece, move)) {
                            return false;
                        }
                    }
                }
            }
        }

        // ** only runs if there are no possible moves (indicates win for other team) **
        // //
        return true;
    }

    /**
     * Checks the state of the model for a certain color. Checks for checkmate
     * and check for each color.
     *
     * @return the new game state
     */
    public GameState checkState() {
        if (inCheckmate(Color.red)) {
            this.gameState = GameState.RED_CHECKMATE;
        } else if (inCheckmate(Color.black)) {
            this.gameState = GameState.BLACK_CHECKMATE;
        } else if (inCheck(this.board, Color.red)) {
            this.gameState = GameState.RED_CHECK;
        } else if (inCheck(this.board, Color.black)) {
            this.gameState = GameState.BLACK_CHECK;
        }
        return this.getGameState();
    }

    /**
     * Prints the current game board for debugging.
     */
    public void printGameBoard() {
        System.out.println("\n\nTurn " + this.getNumTurns() + ":\n");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.getBoard()[i][j] == null) {
                    System.out.print("X");
                } else {
                    System.out.print(this.getBoard()[i][j].getText());
                }
                if (j < 7) {
                    System.out.print(" | ");
                }
            }
            if (i < 8) {
                System.out.println("\n-------------------------------------");
            }
        }
    }

    // **************************************************************************
    // * Getters
    // **************************************************************************

    public Piece[][] getBoard() {
        Piece[][] boardCopy = new Piece[9][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if (piece != null) {
                    boardCopy[i][j] = piece.copy();
                }
            }
        }
        return boardCopy;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public Piece getPieceInPlay() {
        return pieceInPlay.copy();
    }

    public Map<Color, General> getGenerals() {
        Map<Color, General> mapCopy = new HashMap<>();
        for (Color c : generals.keySet()) {
            mapCopy.put(c, generals.get(c).copy());
        }
        return mapCopy;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public boolean isRedTurn() {
        return this.redTurn;
    }

    /**
     * Getter for the contents of a cell.
     *
     * @param x column of cell
     * @param y row of cell
     * @return the piece held in that cell
     */
    public Piece getPiece(int x, int y) {
        return this.board[x][y];
    }
    // **************************************************************************
    // * Setters
    // **************************************************************************

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setGenerals(Map<Color, General> generals) {
        this.generals = generals;
    }

    public void setNumTurns(int numTurns) {
        this.numTurns = numTurns;
    }

    public void setPieceInPlay(Piece pieceInPlay) {
        this.pieceInPlay = pieceInPlay;
    }

    public void setRedTurn(boolean redTurn) {
        this.redTurn = redTurn;
    }

    // **************************************************************************
    // * MAIN METHOD
    // **************************************************************************

    /**
     * Main method runs a Chinese Chess game ending in checkmate for Black.
     */
    public static void main(String[] args) {
        ChineseChess c = new ChineseChess();

        c.printGameBoard();

        // red
        c.playMove(new Point(4, 6));
        c.playMove(new Point(4, 5));

        c.printGameBoard();

        // black
        c.playMove(new Point(0, 0));
        c.playMove(new Point(0, 1));

        c.printGameBoard();

        // red
        c.playMove(new Point(4, 5));
        c.playMove(new Point(4, 4));

        // black
        c.playMove(new Point(0, 1));
        c.playMove(new Point(4, 1));

        // red
        c.playMove(new Point(4, 4));
        c.playMove(new Point(4, 3));

        // black
        c.playMove(new Point(0, 3));
        c.playMove(new Point(0, 4));

        // red
        c.playMove(new Point(4, 3));
        c.playMove(new Point(4, 2));

        // black
        c.playMove(new Point(0, 4));
        c.playMove(new Point(0, 5));

        // red
        c.playMove(new Point(1, 7));
        c.playMove(new Point(4, 7));

        // black
        c.playMove(new Point(0, 5));
        c.playMove(new Point(0, 6));

        // red
        c.playMove(new Point(4, 7));
        c.playMove(new Point(4, 6));

        // black
        c.playMove(new Point(0, 6));
        c.playMove(new Point(0, 7));

        // red
        c.playMove(new Point(7, 7));
        c.playMove(new Point(4, 7));

        // black
        c.playMove(new Point(0, 7));
        c.playMove(new Point(0, 8));

        // red
        c.playMove(new Point(4, 2));
        c.playMove(new Point(4, 1));

        System.out.println();
        System.out.print(c.getGameState());

        System.out.println();
        System.out.println();
        System.out.println("Result is: " + c.checkState());
    }
}
