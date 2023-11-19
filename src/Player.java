import java.util.ArrayList;
import java.util.Random;

public class Player
{
    private final int maxDepth;
    private final int playerColour;
    private final Random random;    // we will be using a random boolean to solve ties in minimax values

    public Player(int maxDepth, int playerColour)
    {
        this.maxDepth = maxDepth;
        this.playerColour = playerColour;
        this.random = new Random();
    }

    /**
     * Calls the maximise function or the minimise function, depending on the player's colour. The black colour acts
     * as a maximiser (and therefore calls max), while white acts as a minimiser (and therefore calls min). <br>
     * The a and b parameters (for pruning purposes) are initialised with Integer.MIN_VALUE and Integer.MAX_VALUE respectively.
     * @param board The current state of the board
     * @return The move as calculated by the minimax algorithm
     */
	public Move minimax(Board board) {
        if (playerColour == Board.WHITE) {
            // if the AI has the white disks, it wants to maximise the value it gets out of its move (since Board.WHITE == 1)
            return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            // similarly, if the AI has the black disks, it wants to minimise the value it gets out of its move (since Board.BLACK == -1)
            return min(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    /**
     * @param board The instance of the board that will be explored in this call of the method.
     * @param depth The depth we are currently at.
     * @param alpha The alpha value as has been calculated between min & max as of this call of the method.
     * @param beta The beta value as has been calculated between min & max as of this call of the method.
     * @return The move the maximiser node is predicted to make given the current state of the board.
     */
    public Move max(Board board, int depth, int alpha, int beta) {
        ArrayList<Board> children = new ArrayList<>(board.getChildren(Board.WHITE));    // as white has called max

        /* if we have either reached the max depth allowed or reached a terminal board state, we return
         * the move that brought the board to this state, combined with the value of its heuristic function */
        if (depth == maxDepth || board.isTerminal() || children.isEmpty()) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }

        Move maxMove = new Move(Integer.MIN_VALUE); // to be updated as the child nodes are explored
        int maxValue = maxMove.getValue();

        for (Board child : children) {
            // fetch the move that min is predicted to play (from the level just below this one, hence depth+1)
            Move currentMove = min(child, depth+1, alpha, beta);
            int currentValue = currentMove.getValue();
            if (currentValue >= maxValue) {
                // if the move we are currently exploring has a greater value according to our heuristic function, update maxMove accordingly
                if (currentValue > maxValue) {
                    maxMove.setRow(child.getLastMove().getRow());
                    maxMove.setCol(child.getLastMove().getCol());
                    maxMove.setValue(currentValue);
                    maxValue = currentValue;

                    /* pruning: If the value of the currently recorded best move is greater than (or equal to) beta, we
                     * have no reason to further check this node or its children, as they will never be returned to the
                     * parent no matter what. */
                    if (maxValue >= beta) {
                        break;
                    }

                    // update alpha accordingly
                    alpha = Math.max(alpha, maxValue);
                } else {
                    /* if the move we are currently exploring has a value equal to the current best recorded,
                     * we simulate a coin toss (with the help of random) to decide which of the 2 to keep.
                     * If the hypothetical coin landed on heads, we keep the new value over the current max.  */
                    boolean heads = random.nextBoolean();
                    if (heads) {
                        maxMove.setRow(child.getLastMove().getRow());
                        maxMove.setCol(child.getLastMove().getCol());
                        maxMove.setValue(currentValue);
                    }
                }
            }
        }
        return maxMove;
    }

    /**
     * Works symmetrically to max.
     * @param board The instance of the board that will be explored in this call of the method.
     * @param depth The depth we are currently at.
     * @param alpha The alpha value as has been calculated between min & max as of this call of the method.
     * @param beta The beta value as has been calculated between min & max as of this call of the method.
     * @return The move the minimiser node is predicted to make given the current state of the board.
     */
    public Move min(Board board, int depth, int alpha, int beta) {
        ArrayList<Board> children = board.getChildren(Board.BLACK); // as black has called min

        /* if we have either reached the max depth allowed or reached a terminal board state, we return
         * the move that brought the board to this state, combined with the value of its heuristic function */
        if (board.isTerminal() || depth == this.maxDepth || children.isEmpty()) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }

        Move minMove = new Move(Integer.MAX_VALUE); // to be updated as the child nodes are explored
        int minValue = minMove.getValue();

        for (Board child : children) {
            // fetch the move that max is predicted to play (from the level just below this one, hence depth+1)
            Move currentMove = max(child, depth+1, alpha, beta);
            int currentValue = currentMove.getValue();
            if (currentValue <= minValue) {
                // if the move we are currently exploring has a smaller value according to our heuristic function, make it the new minMove
                if (currentValue < minValue) {
                    minMove.setRow(child.getLastMove().getRow());
                    minMove.setCol(child.getLastMove().getCol());
                    minMove.setValue(currentValue);
                    minValue = currentValue;

                    /* pruning: If the value of the currently recorded best move is smaller than (or equal to) alpha, we
                     * have no reason to further check this node or its children, as they will never be returned to the
                     * parent no matter what. */
                    if (minValue <= alpha) {
                        break;
                    }
                    beta = Math.min(beta, minValue);
                } else {
                    /* Simulating another coin toss in case the proposed moves are of equal value. Here, for the sake
                     * of opposites, we will be naming this boolean variable "tails".      */
                    boolean tails = random.nextBoolean();
                    if (tails) {
                        minMove.setRow(child.getLastMove().getRow());
                        minMove.setCol(child.getLastMove().getCol());
                        minMove.setValue(currentValue);
                    }
                }
            }
        }
        return minMove;
    }
}