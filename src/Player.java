import java.util.ArrayList;
import java.util.Random;

public class Player
{
	private final int maxDepth;
    private final int playerLetter;

    public Player() {
        this(2, Board.BLACK);
    }

    public Player(int maxDepth, int playerLetter)
    {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    /**
     * Calls the maximise function or the minimise function, depending on the player's colour. The black colour acts
     * as a maximiser (and therefore calls max), while white acts as a minimiser (and therefore calls min). <br>
     * The a and b parameters (for pruning purposes) are initialised with Integer.MIN_VALUE and Integer.MAX_VALUE respectively.
     * @param board The current state of the board
     * @return The move as calculated by the minimax algorithm
     */
	public Move minimax(Board board) {
        if (playerLetter == Board.BLACK) {
            return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            return min(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }
	
	public Move max(Board board, int depth, int alpha, int beta) {
        Random rand = new Random();
        ArrayList<Board> children = board.getChildren(Board.BLACK); // as black has called max

        /* if we have either reached the max depth allowed or reached a terminal board state, we return
         * the move that brought the board to this state, combined with the value of its heuristic function */
        if (board.isTerminal() || depth == this.maxDepth || children.isEmpty()) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }

        Move maxMove = new Move(Integer.MIN_VALUE); // to be updated as the child nodes are explored
        int maxValue = maxMove.getValue();
        for (Board child : children) {
            Move currentMove = min(child, depth+1, alpha, beta);
            int currentValue = currentMove.getValue();
            if (currentValue >= maxValue) {
                // if the move we are currently exploring has a greater value according to our heuristic function, make it the new maxMove
                if (currentValue > maxValue) {
                    maxMove.update(currentMove);
                    maxValue = currentValue;

                    /* pruning: If the max value currently recorded exceeds (or is equal to) beta, we have no reason to
                     * further check this node's children, as they will never be returned to the parent no matter what. */
                    if (currentValue >= beta) {
                        return maxMove;
                    }
                    // update alpha accordingly
                    alpha = Math.max(alpha, currentValue);

                } else {
                    /* if the move we are currently exploring has a value equal to the current best recorded,
                     * we simulate a coin toss (with the help of rand) to decide which of the 2 to keep.
                     * If the hypothetical coin landed on heads, we keep the new value over the current max.    */
                    boolean heads = rand.nextBoolean();
                    if (heads) {
                        maxMove.update(currentMove);
                    }
                }
            }
        }
        return maxMove;
    }
	
	public Move min(Board board, int depth, int alpha, int beta) {
        Random rand = new Random();
        ArrayList<Board> children = board.getChildren(Board.WHITE); // as white has called min

        /* if we have either reached the max depth allowed or reached a terminal board state, we return
         * the move that brought the board to this state, combined with the value of its heuristic function */
        if (board.isTerminal() || depth == this.maxDepth || children.isEmpty()) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }

        Move minMove = new Move(Integer.MAX_VALUE); // to be updated as the child nodes are explored
        int minValue = minMove.getValue();

        for (Board child : children) {
            Move currentMove = max(child, depth+1, alpha, beta);
            int currentValue = currentMove.getValue();
            if (currentValue <= minValue) {
                // if the move we are currently exploring has a smaller value according to our heuristic function, make it the new minMove
                if (currentValue < minValue) {
                    minMove.update(currentMove);
                    minValue = currentValue;

                    if (currentValue <= alpha) {
                        return minMove;
                    }
                    beta = Math.min(beta, currentValue);
                } else {
                    /* Similarly, simulating another coin toss in case the proposed moves are of equal value.
                     * Here, for the sake of opposites, we will be naming this boolean variable "tails".      */
                    boolean tails = rand.nextBoolean();
                    if (tails) {
                        minMove.update(currentMove);
                    }
                }
            }
        }
        return minMove;
    }
}