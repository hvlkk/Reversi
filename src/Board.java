import javax.naming.NameClassPair;
import java.util.ArrayList;

public class Board
{
	public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;
    /*  TODO: Remove if no workaround found
    private static final String WHITE_DISK = "⚪";
    private static final String BLACK_DISK = "⚫";
    private static final String EMPTY_SQUARE = "◻f";
     */
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
    }

    /**
     * @param letter The player making the move
     * @return An arraylist of all the possible states we can get to from the board as is currently
     */
	public ArrayList<Board> getChildren(int letter) {return null;}

    /**
     * The heuristic function. TODO: more complete desc
     * @return The result of the heuristic function
     */
	public int evaluate () {return 0;}

    /**
     * Making the assumption that the game does not end when a player is out of moves (as happens in some variations of
     * the game), therefore the only way a state can be terminal is if the entire board is full.
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

    /**
     * @param row The row entered
     * @param col The column entered
     * @return True if the move entered is valid, false otherwise
     */
    private boolean isValidMove(int row, int col) {
        // if the square entered is out of bounds
        if (row < 0 || col < 0 || row >= ROWS || col >= COLUMNS) {
            return false;
        }

        // if the square entered is already occupied
        if (gameBoard[row][col] != EMPTY) {
            return false;
        }

        /* making a custom [8][2] 2D array called dimensions; each of the 8 rows consists of a "pair" (due to the lack
         * of a native Java Pair class), the "key" of which represents the difference in rows to the board square provided
         * as a parameter to this function, and the "value" of which similarly represents the difference in columns to the
         * index we are currently checking.    */
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
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
	
	
}