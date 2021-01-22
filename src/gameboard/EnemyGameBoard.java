package gameboard;

public class EnemyGameBoard {
    int points;

    public enum boardStates{
        WATER, // Blue
        SHOT_WATER, // Black
        SHOT_SHIP // Red
    }

    private String[][] board;


    public EnemyGameBoard() {
        board = new String[15][10];
        board = initialiseEmptyBoard(board);
        points = 0;
    }

    /**
     * Checks whether a move isn't made on a field that is outside the board or already been shot.
     * @param x The x coordinate of the move
     * @param y The y coordinate ofthe move
     * @return Whether the move is valid
     */
    public boolean isValidMove(int x, int y) {
        return (
            x >= 0 && 
            x <= 14 && 
            y >= 0 && 
            y <= 9 && 
            !board[x][y].equals("SHOT_SHIP") &&
            !board[x][y].equals("SHOT_WATER")
        );
    }

    /**
     * Updates enemy board given coordinates of the move and whether or not it was a hit and whether a ship sunk.
     * Points are added when ship is hit and when ships sinks.
     * @param x X coordinate of the move.
     * @param y Y coordinate of the move.
     * @param isHit Indicates whether a ships was hit.
     * @param isSunk Indicates whether a boat sunk.
     */
    public void makeMove(int x, int y, boolean isHit, boolean isSunk) {
        if (isHit) {
            board[x][y] = "SHOT_SHIP";
            points++;
        } else {
            board[x][y] = "SHOT_WATER";
        }

        if (isSunk) {
            points++;
        }
    }

   /**
     * Initialises a given board by setting all fields of it to the string WATER
     * @param board the board to be initalised with water fields
     * @return the initialised board.
     */
    public String[][] initialiseEmptyBoard(String[][] board) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = "WATER";
            }
        }
        return board;
    }

}
