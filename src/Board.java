import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board
{
    public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;

    private final int[][] gameBoard;

    private int lastPlayer;
    private int whiteScore;
    private int blackScore;

    private Move lastMove;

	public Board() {
        this.gameBoard = new int[ROWS][COLUMNS];
        this.lastMove = new Move();
        this.lastPlayer = WHITE;    // as black plays first
        initialiseBoard();
        this.whiteScore = 2;
        this.blackScore = 2;
    }
	
	// copy constructor
    public Board(Board board) {
        this.gameBoard = new int[ROWS][COLUMNS];
        this.lastMove = new Move(board.lastMove);
        this.lastPlayer = board.lastPlayer;
        for (int row = 0; row < 8; ++row) {
            System.arraycopy(board.gameBoard[row], 0, this.gameBoard[row], 0, 8);
        }
        this.whiteScore = board.whiteScore;
        this.blackScore = board.blackScore;
    }

    /**
     * Initialises the board to the "start state", following the default convention. <br> All squares are empty except from
     * the centre 4, which (cols in letters, rows in numbers) will consist of white pieces on d4 & e5 and black on d5 & e4.
     */
    private void initialiseBoard() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                this.gameBoard[row][col] = EMPTY;
            }
        }

        // separate for, for the 2 middle rows (3 and 4)
        for (int row = 3; row < 5; ++row) {
            int col;

            // updating the first 3 columns
            for (col = 0; col < 3; ++col) {
                this.gameBoard[row][col] = EMPTY;
            }

            // updating [row][3]
            if (col==row) {
                this.gameBoard[row][col++] = WHITE;
            } else {
                this.gameBoard[row][col++] = BLACK;
            }

            // updating [row][4]
            if (col==row) {
                this.gameBoard[row][col] = WHITE;
            } else {
                this.gameBoard[row][col] = BLACK;
            }

            // updating the 3 remaining columns
            for (++col ; col < COLUMNS; ++col) {
                this.gameBoard[row][col] = EMPTY;
            }
        }
        for (int row = 5; row < ROWS; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                this.gameBoard[row][col] = EMPTY;
            }
        }
    }
	
	public void print() {
        System.out.println("_______________________");
        System.out.println("| \\ | A B C D E F G H |");
        System.out.println("|___|_________________|");
        for (int row = 0; row < ROWS; row++) {
            System.out.print("| ");
            System.out.print((row + 1) + " | ");
            for (int col = 0; col < COLUMNS; col++) {
                switch (this.gameBoard[row][col]) {
                    case WHITE -> System.out.print("W ");
                    case BLACK -> System.out.print("B ");
                    case EMPTY -> System.out.print("_ ");
                    default -> {
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("|___|_________________|");
        System.out.println("Current scores:");
        System.out.println("Black: " + blackScore);
        System.out.println("White: " + whiteScore);
    }

    /**
     * Used to check whether a player has moves available to him, or if they have to forfeit their turn.
     * @param playerColour The colour of the player we check for.
     * @return True if the player can play, false if not.
     */
    public boolean canPlay(int playerColour) {
        if (playerColour == WHITE) {
            return whiteScore > 0 && !getChildren(playerColour).isEmpty();
        } else if (playerColour == BLACK) {
            return blackScore > 0 && !getChildren(playerColour).isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Generates the children of the current state of the board.
     * @param playerColour The player making the move
     * @return An arraylist of all the possible states we can get to from the board as is currently
     */
	public ArrayList<Board> getChildren(int playerColour) {
        ArrayList<Board> children = new ArrayList<>();
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                if (isValidMove(row, col, playerColour)) {
                    Board child = new Board(this);
                    child.makeMove(row, col, playerColour);
                    children.add(child);
                }
            }
        }
        return children;
    }

    /**
     * The heuristic function. Takes multiple scenarios into consideration, including corner squares, squares at the
     * edges of the board, squares that are considered slightly more dangerous, etc.
     * @return The result of the heuristic function
     */
    // TODO: more complete desc
	public int evaluate () {
        int evaluation = 0; // will mark the final result of our heuristic function

        // if this play leads to the game ending
        if (isTerminal()) {
            if (blackScore > whiteScore) {
                evaluation += 4000;
            } else if (whiteScore > blackScore) {
                evaluation -= 4000;
            }
        }

        // if this play leads to a player's opponent not being able to play
        if (!canPlay(BLACK)) {
            evaluation -= 2000;
        } else if (!canPlay(WHITE)) {
            evaluation += 2000;
        }


        int cornerEvaluation = gameBoard[0][0] + gameBoard[0][7] + gameBoard[7][0] + gameBoard[7][7];
        int edgeEvaluation = 0;
        int pieceEvaluation = 0;
        int dangerEvaluation = 0;

        for(int row = 0; row < ROWS; ++row){
            for (int col = 0; col < COLUMNS; ++col){
                pieceEvaluation += gameBoard[row][col];

                /* a square being marked as dangerous works against the player who puts a disk there. Therefore, taking
                 * away from the current score based on the colour of the piece. */
                if (indexIsDangerous(row, col)) {
                    dangerEvaluation -= gameBoard[row][col];
                }

                // edges: checking the pieces that are at the edges of the board, but not at the corners
                if(indexIsAtEdges(row, col)){
                    edgeEvaluation += gameBoard[row][col];
                }
            }
        }
        evaluation += 285 * cornerEvaluation + 85 * dangerEvaluation + 70 * edgeEvaluation + 35 * pieceEvaluation;
        return evaluation;
    }

    /**
     * Helper function, returns whether a square is inside the board.
     * @param row The row of the square.
     * @param col The column of the square.
     * @return True if the square is inside the board.
     */
    private boolean indexInBounds(int row, int col) {
        return row >=0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    /**
     * Helper function, returns whether a square is at the edges of the board.
     * @param row The row of the square.
     * @param col The column of the square.
     * @return True if the square is at the edges of the board.
     */
    private boolean indexIsAtEdges(int row, int col) {
        boolean topEdge = row == 0 && col != 0 && col != COLUMNS - 1;
        boolean bottomEdge = row == ROWS - 1 && col != 0 && col != COLUMNS - 1;
        boolean leftEdge = col == 0 && row != 0 && row != ROWS - 1;
        boolean rightEdge = col == COLUMNS -1 && row != 0 && row != ROWS - 1;
        return topEdge || bottomEdge || leftEdge || rightEdge;
    }

    /**
     * Helper function, returns whether a square is in the corners of the board.
     * @param row The row of the square.
     * @param col The column of the square.
     * @return True if the square is in the corners of the board.
     */
    private boolean indexIsCorner(int row, int col) {
        return  (row == 0 || row == ROWS-1) && (col == 0 || col == COLUMNS-1);
    }

    /**
     * Helper function, returns whether an index is in a square that is identified as dangerous. A dangerous square is a
     * square that is only one square away (vertically, horizontally or diagonally) from a corner square.
     * @param row The row to be checked.
     * @param col The column to be checked.
     * @return True if the square is a "danger square", false otherwise.
     */
    private boolean indexIsDangerous(int row, int col) {
        if (indexIsCorner(row, col)) {
            return false;
        }

        /* an index is considered dangerous if the sum of its row and col is <= the sum of the row and col of a corner
         * square + 2 (to accommodate for horizontal, vertical and diagonal directions. */
        boolean topLeft = (row + col) - (0 + 0) <= 2 && (row <= 1) && (col <= 1);
        boolean topRight = (row + col) - (0 + 7) <= 2 && (row <= 1) && (col <= COLUMNS - 1);
        boolean bottomLeft = (row + col) - (7 + 0) <= 2 && (row <= ROWS - 1) && (col <= 1);
        boolean bottomRight = (row + col) - (7 + 7) <= 2 && (row <= ROWS - 1) && (col <= COLUMNS - 1);
        return topLeft || topRight || bottomLeft || bottomRight;
    }

    /**
     * @param row The row entered.
     * @param col The column entered.
     * @return True if the move entered is valid, false otherwise
     */
    private boolean isValidMove(int row, int col, int playerColour) {
        // if the square entered is out of bounds
        if (row < 0 || col < 0 || row >= ROWS || col >= COLUMNS) {
            return false;
        }

        // if the square entered is already occupied
        if (gameBoard[row][col] != EMPTY) {
            return false;
        }

        int opponentColour = playerColour * -1;

        /* making a custom [8][2] 2D array called dimensions; each of the 8 rows consists of a "pair" (due to the lack
         * of a native Java Pair class), the "key" of which represents the difference in rows to the board square provided
         * as a parameter to this function, and the "value" of which similarly represents the difference in columns to the
         * board square we are currently checking.    */
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] direction: directions) {
            int rowToExamine = row + direction[0];
            int colToExamine = col + direction[1];
            // initialising a counter for flanked pieces, for each direction
            int flankedPieces = 0;

            // if the index we are exploring is outside the board or refers to a corner square, continue
            if (!indexInBounds(rowToExamine, colToExamine)) {
                continue;
            }

            // while we keep coming across opponent pieces and the index does not go out of bounds, continue exploring
            while (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == opponentColour) {
                ++flankedPieces;
                rowToExamine += direction[0];
                colToExamine += direction[1];
            }

            // if the loop ended because we found a piece of the player's, that means the move is valid
            if (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == playerColour && flankedPieces > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called once we have ensured that a move is valid. Updates the disk at the square given as parameter to the function,
     * to the colour of the player making the move, and flips all related opponent disks accordingly.
     * @param row The row entered.
     * @param col The column entered.
     * @param playerColour The colour of the player making the move
     */
    public void makeMove(int row, int col, int playerColour) {
        // precautionary check
        if (!isValidMove(row, col, playerColour)) {
            return;
        }

        // placing the piece and making the necessary score adjustments
        gameBoard[row][col] = playerColour;
        if (playerColour == WHITE) {
            ++whiteScore;
        } else {
            ++blackScore;
        }
        setLastMove(new Move(row, col));
        setLastPlayer(playerColour);

        int opponentColour = playerColour * -1;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] direction: directions) {
            int rowToExamine = row + direction[0];
            int colToExamine = col + direction[1];

            // if the index we are exploring is outside the board or refers to a corner square, continue
            if (!indexInBounds(rowToExamine, colToExamine) || indexIsCorner(rowToExamine, colToExamine)) {
                continue;
            }

            // initialising a counter for flanked pieces, for each direction
            int flankedPieces = 0;

            // while we keep coming across opponent pieces and the index does not go out of bounds, continue exploring
            while (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == opponentColour) {
                ++flankedPieces;
                rowToExamine += direction[0];
                colToExamine += direction[1];
            }

            // if the loop ended because we found a piece of the player's, update accordingly
            if (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == playerColour && flankedPieces > 0) {
                // edit the scores accordingly
                if (playerColour == WHITE) {
                    whiteScore += flankedPieces;
                    blackScore -= flankedPieces;
                } else {
                    blackScore += flankedPieces;
                    whiteScore -= flankedPieces;
                }

                // and edit the board accordingly
                int rowToEdit = row + direction[0];
                int colToEdit = col + direction[1];

                for (int i = 0; i < flankedPieces; ++i) {
                    gameBoard[rowToEdit][colToEdit] = playerColour;
                    rowToEdit += direction[0];
                    colToEdit += direction[1];
                }
            }
        }
    }

    /**
     * Making the assumption that the game ends if the board is full, if one player has no disks left on the board, or
     * if both players have no valid moves left. <b>Overly complex conditions where a player is mathematically
     * impossible to win will not be checked.</b>
     * @return True if the game has ended according to the above conditions, false if it has not.
     */
    public boolean isTerminal() {
        if (whiteScore == 0 || blackScore == 0 || (!canPlay(WHITE) && !canPlay(BLACK))) {
            return true;
        }

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                if (gameBoard[row][col] == EMPTY) {
                    return false;
                }
            }
        }
        // if all the squares have been filled
        return true;
    }
	
	public Move getLastMove()
    {
        return this.lastMove;
    }

    public void setLastMove(Move lastMove)
    {
        this.lastMove = new Move(lastMove);
    }

    public void setLastPlayer(int lastPlayer)
    {
        this.lastPlayer = lastPlayer;
    }

    /**
     * @return The count of disks on the board for the player whose colour is passed as parameter. Returns
     * Integer.MIN_VALUE if passed an illegal argument.
     */
    public int getScore(int playerColour) {
        if (playerColour == Board.WHITE) {
            return whiteScore;
        } else if (playerColour == Board.BLACK) {
            return blackScore;
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Called to calculate the winner of the game.
     * @return Board.WHITE (1) if white won, Board.BLACK (-1) if black won, Board.EMPTY (0) if the game ended as a tie,
     * and Integer.MIN_VALUE (as a sign of error) if the game is not over yet.
     */
    public int getWinner() {
        // precautionary check
        if (!isTerminal()) {
            return Integer.MIN_VALUE;
        }

        if (whiteScore > blackScore) {
            return WHITE;
        } else if (blackScore > whiteScore) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return lastPlayer == board.lastPlayer && whiteScore == board.whiteScore && blackScore == board.blackScore && Arrays.deepEquals(gameBoard, board.gameBoard) && Objects.equals(lastMove, board.lastMove);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(lastPlayer, whiteScore, blackScore, lastMove);
        result = 31 * result + Arrays.deepHashCode(gameBoard);
        return result;
    }
}