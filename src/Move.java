public class Move
{
    private int row;
    private int col;
    private int value;

    public Move()
    {
        this.row = -1;
        this.col = -1;
        this.value = 0;
    }

    public Move(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.value = -1;
    }

    public Move(int value)
    {
        this.row = -1;
        this.col = -1;
        this.value = value;
    }

    public Move(int row, int col, int value)
    {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow()
    {
        return this.row;
    }

    public int getCol()
    {
        return this.col;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Updates the contents of the Move instance this method is called upon to the contents of the Move instance passed as parameter.
     * @param other The Move instance the contents of which we will be keeping.
     */
    public void update(Move other) {
        this.setRow(other.getRow());
        this.setCol(other.getCol());
        this.setValue(other.getValue());
    }

    /**
     * Used to return the square of the board that a Move corresponds to in a format that is easily readable by the user (e.g. D4 or E5)
     */
    public String formattedIndex() {
        // converting the row to a letter and the col to 1-based index
        char rowChar = (char) ('A' + row);
        return String.valueOf(rowChar) + (col + 1);
    }

    /**
     * Used to convert the input from the user to a valid Move instance.
     * @param input The string that the user has entered (e.g. D4 or E5)
     * @return The move that the user entered.
     */
    public static Move readFormattedMove(String input) {
        input = input.strip();
        // precautionary check, if the input is invalid we return the default Move
        if (input.length() != 2 || !isValidRowChar(input.charAt(0)) || !isValidColIndex(input.charAt(1))) {
            return new Move();
        }
        char rowChar = Character.toUpperCase(input.charAt(0));
        int row = rowChar - 'A';
        int col = Character.getNumericValue(input.charAt(1)) - 1;

        return new Move(row, col);
    }

    /**
     * Ensures the character used to represent the row is valid and not outside the bounds. Accepts both uppercase and lowercase characters.
     * @param c The character that represents the row.
     * @return True if the character is a valid row index.
     */
    private static boolean isValidRowChar(char c) {
        return (c >= 'A' && c <= 'H') || (c >= 'a' && c <= 'h');
    }

    /**
     * Ensures the character used to represent the column is valid and not outside the bounds.
     * @param c The character that represents the column.
     * @return True if the character is a valid column index.
     */
    private static boolean isValidColIndex(char c) {
        return c >= '1' && c <= '8';
    }

    /**
     * Used to check whether a move is the default move, aka initialised with the default constructor.
     * @return True if it is the default move, false otherwise.
     */
    public boolean isDefault() {
        return this.row == -1 && this.col == -1;
    }
}
