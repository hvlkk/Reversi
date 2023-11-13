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
        Board board = new Board();
        board.print();
    }

}