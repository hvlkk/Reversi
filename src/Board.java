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

    private int[][] gameBoard;

    private int lastPlayer;
    private int whiteScore;
    private int blackScore;
    private int winner;

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
        this.lastMove = board.lastMove;
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
     * The heuristic function. TODO: more complete desc
     * @return The result of the heuristic function
     */
	public int evaluate () {return 0;}

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

            // if the index we are exploring is outside the board or refers to a corner square, continue
            if (!indexInBounds(rowToExamine, colToExamine) || indexIsCorner(rowToExamine, colToExamine)) {
                continue;
            }

            // while we keep coming across opponent pieces and the index does not go out of bounds, continue exploring
            while (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == opponentColour) {
                rowToExamine += direction[0];
                colToExamine += direction[1];
            }

            // if the loop ended because we found a piece of the player's, that means the move is valid
            if (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == playerColour) {
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
        this.lastMove = new Move(row, col);

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
            int flankedPieces = 0;  // TODO: Make sure the calculations are as intended

            // while we keep coming across opponent pieces and the index does not go out of bounds, continue exploring
            while (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == opponentColour) {
                ++flankedPieces;
                rowToExamine += direction[0];
                colToExamine += direction[1];
            }

            // if the loop ended because we found a piece of the player's, update accordingly
            if (indexInBounds(rowToExamine, colToExamine) && gameBoard[rowToExamine][colToExamine] == playerColour) {
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

                while (rowToEdit != rowToExamine && colToEdit != colToExamine) {
                    gameBoard[rowToEdit][colToEdit] = playerColour;
                    rowToEdit += direction[0];
                    colToEdit += direction[1];
                }
            }
        }
    }

    /**
     * Making the assumption that the game does not end when a player is out of moves (as happens in some variations of
     * reversi), therefore the only way a state can be terminal is if the entire board is full.
     * @return True if the game has ended, false if it has not.
     */
    public boolean isTerminal() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
                if (gameBoard[row][col] != EMPTY) {
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

    public int getLastPlayer()
    {
        return this.lastPlayer;
    }

    public int[][] getGameBoard()
    {
        return this.gameBoard;
    }
	
	public void setGameBoard(int[][] gameBoard)
    {
        for(int row = 0; row < ROWS; row++)
        {
            System.arraycopy(gameBoard[row], 0, this.gameBoard[row], 0, COLUMNS);
        }
    }

    public void setLastMove(Move lastMove)
    {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    public void setLastPlayer(int lastPlayer)
    {
        this.lastPlayer = lastPlayer;
    }

    /**
     * Calculates the scores for both players, and the winner (if there is one) as a result
     */
    public void calculateResults() {

    }

    /**
     * Helper function, returns whether a square is inside the board.
     * @param row The row of the square.
     * @param col The column of the square.
     * @return True if the square is inside the board
     */
    private boolean indexInBounds(int row, int col) {
        return row >=0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    /**
     * Helper function, returns whether a square is in the corners of the board.
     * @param row The row of the square.
     * @param col The column of the square.
     * @return True if the square is in the corners of the board
     */
    private boolean indexIsCorner(int row, int col) {
        return  (row == 0 || row == ROWS-1) && (col == 0 || col == COLUMNS-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return lastPlayer == board.lastPlayer && whiteScore == board.whiteScore && blackScore == board.blackScore && winner == board.winner && Arrays.deepEquals(gameBoard, board.gameBoard) && Objects.equals(lastMove, board.lastMove);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(lastPlayer, whiteScore, blackScore, winner, lastMove);
        result = 31 * result + Arrays.deepHashCode(gameBoard);
        return result;
    }
}