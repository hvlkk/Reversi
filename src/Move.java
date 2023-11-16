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
     * Used to return the square of the board that a Move corresponds to in a format that is easily readable by the user (e.g. D4, E5)
     */
    public String formattedIndex() {

        // converting the row to a letter and the col to 1-based index
        char rowChar = (char) ('A' + row);
        return String.valueOf(rowChar) +
                (col + 1);
    }
}
