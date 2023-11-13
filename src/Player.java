public class Player
{
	private int maxDepth;
    private int playerLetter;
    private int a = Integer.MIN_VALUE;
    private int b = Integer.MAX_VALUE;

    public Player() {
        this(2, Board.BLACK);
    }

    public Player(int maxDepth, int playerLetter)
    {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }
	
	public Move minimax(Board board) {return null;}
	
	public Move max(Board board, int depth) {return null;}
	
	public Move min(Board board, int depth) {return null;}
}