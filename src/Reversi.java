import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class containing the program's main method.
 */
public class Reversi {
    public static void main(String[] args) {
        reversiLoop();
    }

    /**
     * The method where the main loop of our reversi program occurs.
     */
    private static void reversiLoop() {
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
        while (!(answer.contains("yes") || answer.contains("no"))) {
            System.out.println("Would you like to play first? Answer yes or no.");
            answer = scanner.nextLine().toLowerCase().strip();
        }
        boolean npcPlays;
        Player npc;
        int npcColour;
        int humanColour;
        if (answer.equals("yes")) {
            npc = new Player(depth, Board.WHITE);
            humanColour = Board.BLACK;
            npcColour = Board.WHITE;
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

                /* fetching the children of the current state of the board; to be used in order to print available moves
                 * to the user, in order to better the user experience.  */
                ArrayList<Board> children = board.getChildren(humanColour);

                // if there is no available move, print an appropriate message and pass the turn to AI again
                if (children.isEmpty()) {
                    System.out.println("There is no available move to be made! AI plays again.");
                    npcPlays = true;
                    continue;
                }
                System.out.println("Please enter the square you wish to place a disk in.");
                System.out.print("Available moves: ");
                for (int i = 0; i < children.size() - 1; ++i) {
                    System.out.print(children.get(i).getLastMove().formattedIndex() + ", ");
                }
                System.out.println(children.get(children.size()-1).getLastMove().formattedIndex() + ".");
                npcPlays = true;
            } else {
                break;
            }
        }

    }
}