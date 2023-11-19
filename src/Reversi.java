import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * The class containing the program's main method.
 */
public class Reversi {
    public static void main(String[] args) {
        try {
            reversiLoop();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * The method where the main loop of our reversi program occurs.
     */
    private static void reversiLoop() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("REVERSI");

        /* in order to make the menu more user-friendly, we will be providing 3 difficulty options instead of letting
         * the user pick freely. Available options are easy (depth of 2), normal (depth of 4), and hard (depth of 8). */
        String difficulty = " ";
        while (!difficulty.equals("1") && !difficulty.equals("2") && !difficulty.equals("3") &&
                !difficulty.equals("Easy") && !difficulty.equals("Normal") && !difficulty.equals("Hard")) {
            System.out.println("Available difficulties:");
            System.out.println("1. Easy");
            System.out.println("2. Normal");
            System.out.println("3. Hard");
            System.out.println("Select difficulty:");
            difficulty = scanner.nextLine().toLowerCase().strip();
        }

        int depth;
        if (difficulty.equals("1") || difficulty.equals("Easy")) {
            depth = 2;
        } else if (difficulty.equals("2") || difficulty.equals("Normal")) {
            depth = 4;
        } else {
            depth = 8;
        }

        String answer = " ";
        while (!(answer.equals("yes") || answer.equals("y") || answer.equals("no") || answer.equals("n"))) {
            System.out.println("Would you like to play first? Answer yes or no.");
            answer = scanner.nextLine().toLowerCase().strip();
        }
        boolean npcPlays;
        Player npc;
        int npcColour;
        int humanColour;
        if (answer.equals("yes") || answer.equals("y")) {
            npc = new Player(depth, Board.WHITE);
            npcColour = Board.WHITE;
            humanColour = Board.BLACK;
            npcPlays = false;
        } else {
            npc = new Player(depth, Board.BLACK);
            npcColour = Board.BLACK;
            humanColour = Board.WHITE;
            npcPlays = true;
        }
        Board board = new Board();
        board.print();

        // main loop
        while (!board.isTerminal()) {
            // if it is the human's turn
            if (!npcPlays) {
                System.out.println("Human plays!");

                // if there is no available move, print an appropriate message and pass the turn to AI again
                if (!board.canPlay(humanColour)) {
                    System.out.println("There is no available move to be made! AI plays again.");
                    board.setLastPlayer(humanColour);
                    npcPlays = true;
                    continue;
                }

                // fetching the children of the current state of the board, in order to print available moves to the user
                ArrayList<Board> children = board.getChildren(humanColour);

                ArrayList<Move> availableMoves = new ArrayList<>();
                for (Board child : children) {
                    availableMoves.add(child.getLastMove());
                }

                //sorting moves so that it's friendlier visually
                availableMoves.sort(
                        Comparator.comparingInt(Move::getCol)
                                .thenComparingInt(Move::getRow)
                );

                Move userMove = new Move();

                while (!availableMoves.contains(userMove)) {
                    // list the available moves (for user-friendliness)
                    System.out.print("Available moves: ");
                    for (int i = 0; i < availableMoves.size() - 1; ++i) {
                        System.out.print(availableMoves.get(i).formattedIndex() + ", ");
                    }
                    System.out.println(availableMoves.get(availableMoves.size()-1).formattedIndex() + ".");

                    // prompt the user for his move of choice
                    System.out.println("Please enter the move you would like to make:");
                    String formattedMove = scanner.nextLine();
                    userMove = Move.readFormattedMove(formattedMove);

                    // appropriate message if the move is not allowed
                    if (!availableMoves.contains(userMove)) {
                        System.out.println("Invalid move! Please enter a move from the available moves listed.");
                    }
                }
                board.makeMove(userMove.getRow(), userMove.getCol(), humanColour);
                board.print();
                npcPlays = true;
            } else {
                System.out.println("AI plays!");

                if (!board.canPlay(npcColour)) {
                    System.out.println("There is no available move to be made! Human plays again.");
                    board.setLastPlayer(npcColour);
                    npcPlays = false;
                    continue;
                }

                ArrayList<Board> children = board.getChildren(npcColour);
                ArrayList<Move> moves = new ArrayList<>();
                for (Board child: children) {
                    moves.add(child.getLastMove());
                }

                //sorting moves so that it's friendlier visually
                moves.sort(
                        Comparator.comparingInt(Move::getCol)
                                .thenComparingInt(Move::getRow)
                );

                System.out.print("Available moves: ");
                for (int i = 0; i < moves.size() - 1; ++i) {
                    System.out.print(moves.get(i).formattedIndex() + ", ");
                }
                System.out.println(moves.get(moves.size()-1).formattedIndex() + ".");

                Move npcMove = npc.minimax(board);

                // introducing a slight delay in cases that are not expensive by nature
                if (!(depth == 8 && children.size() >= 5)) {
                    sleep(1000);
                }
                System.out.println("AI move: " + npcMove.formattedIndex());
                board.makeMove(npcMove.getRow(), npcMove.getCol(), npcColour);
                board.print();
                npcPlays = false;
            }
        }

        int winner = board.getWinner();
        if (winner != Board.BLACK && winner != Board.WHITE && winner != Board.EMPTY) {
            // if calling getWinner fetched the error value instead of the acceptable values
            throw new RuntimeException("Unexpected outcome.");
        }

        System.out.println();
        System.out.println("Game over!");
        System.out.println("Final scores: ");
        System.out.println("Human: " + board.getScore(humanColour));
        System.out.println("AI: " + board.getScore(npcColour));
        System.out.println();

        if (winner == humanColour) {
            System.out.println("You defeated our AI! Congratulations.");
        } else if (winner == npcColour) {
            System.out.println("You lost. Try again! You can pull through.");
        } else {
            System.out.println("The game ended in a tie!");
        }
    }
}