=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - The game board is stored as a 2D array of Piece objects. It allows
  iteration through the array to iterate through all game pieces in action. It also
  allows for a specific piece at a known location to be accessed. There are no cases of
  ArrayOutOfBoundsExceptions, as all calls were made within the bounds of the array. There
  is no redundancy in 2D Array use, as only one array is used to store the game board at
  all times. Encapsulation is preserved, as the board array is only accessed within
  the ChineseChess class and a clone of the board array is created in order to test if
  each side is in check. This array was the best option to store game pieces because of
  the grid-based nature of the game board and the dependency of movement and attacks on
  the surrounding pieces. With a collection of pieces, movement patterns and such would
  have to iterate through ALL pieces, while an array allows only certain rows and columns
  to be accessed. The board stores Piece objects rather than points or other types
  because it is easiest to access the Piece information through the objects themselves.
  Piece was used instead of specific subtypes because the board needed to contain all
  subtypes of pieces.

  2. Inheritance and Subtyping - This games uses a Piece abstract class to model the
  instance variables and draw functions for all types of pieces. Subtypes Soldier,
  Cannon, Chariot, Horse, Elephant, Guard and General all extend this abstract class.
  An abstract class was used because the subtypes needed to inherit draw functions as
  well as getters and setters for various instance variables. In addition, it never
  needed to be instantiated as a simple "Piece", so a normal type was unnecessary. Each
  of the subclasses then has its own getMoveSet function because each piece has its own
  move and capture rules. This cannot be stored in an instance variable because each piece
  needs its own complex method to retrieve possible moves. Dynamic Dispatch is present
  when calling this function to the Piece array - even though the pieces are stores as
  Piece objects, the getMoveSet() function is overridden by each subclass, so it calls
  that subtype's function instead.

  3. Collections - Collections (Sets) store the set of possible moves for each piece.
  The pieces do not have instance variables to store such moves because they are changed
  upon each change of the board. Instead, each Piece has a function to get the set of
  moves, which returns a HashSet of Points. These points are then iterated through via a
  for-each loop to determine if the opponent's king is in check. In addition, this
  function is used to determine whether the player's move is legal. Any click is processed
  to make sure that the move set contains the move (using .contains(), not iteration).
  The sets are not modified after being generated, so there are no
  ConcurrentModificationExceptions thrown. There is no redundancy as the get move set
  function is only called when necessary and the moves are not stored in another set.
  The set is encapsulated because it is only generated and edited within the Piece class.
  A Set was used here, rather than an array because there was no way of determining the
  number of moves in the set before actually finding all of them. Therefore, there was
  no way to pre-determine the length of the set and because a Set can be resized it was
  optimal for this application. A Set was used instead of a List or Map because the items
  do not need to be ordered and there was no need to map them to anything.

  4. Complex Game Logic - All check features are implemented: straightforward check works,
  as an opposing piece with the capability to capture the general. Generals cannot move
  into check and pieces cannot move their own general into check because moves are
  considered illegal if the post-move state leaves their side's general in check. Similarly,
  the player must resolve check to make a legal move because the post-game state would still
  leave their side's general in check if unresolved. Checkmate is implemented in this game as
  well. In Chinese Chess, it is checkmate if your side has no legal moves (not necessarily
  in check). This is implemented by checking for legality of all moves for one side after
  each turn. Soldier promotion is implemented in this game as well. After Soldiers cross
  the midline of the board, the may move both horizontally and laterally (but not backwards).
  Elephant, Guard, and General movement restrictions are implemented as well, as they are
  limited to certain ranges of movement. In addition, Cannon jump captures are implemented
  as they have a special case where they only capture while jumping over another pieces.
  Horse and Elephant blockers were also implemented, as there are cases when their moves are
  split into two separate steps and cannot be completed with blockers in the way. Another bit
  of complex game logic included flying general attacks, which occur only when generals are on
  the same column at the same time with no pieces in between.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  The first created class was the Piece class, which represents an abstract piece
  on the game board. It is extended to the following classes for specific pieces:
  - Soldier, which is a type of piece that moves like a chess pawn (normally)
  - Cannon, which is a type of piece that moves like a chess rook, but has a unique
  capture case (jumping).
  - Chariot, which is a type of piece that moves like a chess rook.
  - Horse, which moves like a chess knight but has some jumping cases allowed and
  some jump cases not allowed.
  - Elephant, which moves like a chess bishop but only 2 spaces on each diagonal.
  - Guard, which moves only within the palace of their general and diagonally one space.
  - General, which moves only within their palace and horizontally/vertically one space.

  Other classes created include:
  - ChessBoard class, which is used as the controller and view for the game.
  - ChineseChess class, which is the actual game model, separate from the controller
  and view, which follows the Model-View-Controller framework.
  - RunChineseChess class, which sets up the GUI through Swing.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  One significant stumbling block was the complexity of checking for checks and
  checkmate, which had to incorporate many types of checks, from regular checks
  to discovered checks to flying king checks and more. In addition, having to test
  the move first then revert it back to its original state was hard.

  Another significant stumbling block was ensuring that Cannon and Chariot pieces
  specifically had the correct move sets, as they added moves until collision with
  another piece.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  My functionality was well-separated given the use of Model-View-Controller framework,
  so view and controller and model were separate. Private states were encapsulated because
  generally getters only returned copies of objects and variables were generally only
  editable within their class.

  I would refactor Chariots and Cannons to potentially extend another abstract
  HorizontallyMovingPiece class, as much of the code was very similar to find move sets.
  In addition, one thing that I noticed was that both the board had coordinates and the
  piece stored its coordinates, so both had to updated after moves. If pieces could be
  refactored to not need to store positions and instead get them from the array itself,
  there would be less redundancy.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  None.