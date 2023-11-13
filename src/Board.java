import java.util.ArrayList;

public class Board
{
	public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;

    private int[][] gameBoard;

    private int lastPlayer;

    private Move lastMove;

	public Board() {
        this.gameBoard = new int[ROWS][COLUMNS];
        this.lastMove = new Move();
        this.lastPlayer = WHITE;    // as black plays first
        initialiseBoard();
    }
	
	// copy constructor
    public Board(Board board) {
        this.gameBoard = new int[ROWS][COLUMNS];
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                this.gameBoard[row][col] = board.gameBoard[row][col];
            }
        }
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

        // separate for, for the 2 middle rows
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
        System.out.println(" ___________________ ");
        System.out.println("| \\ A B C D E F G H |");
        for (int row = 0; row < ROWS; row++) {
            System.out.print("| ");
            System.out.print((row + 1) + " ");
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
        System.out.println("|___________________|");
    }

    /**
     * @param letter the player making the move
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
     * @return True if the game is over, false if the state the board is in is not terminal
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
     * @param row the row entered
     * @param col the column entered
     * @return true if the move entered is valid, false otherwise
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
            for(int col = 0; col < COLUMNS; col++)
            {
                this.gameBoard[row][col] = gameBoard[row][col];
            }
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
	
	
}