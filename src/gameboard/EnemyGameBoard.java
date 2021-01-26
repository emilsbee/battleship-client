package gameboard;

// Internal imports
import constants.GameConstants;

/**
 * This class represent the opponent's board of a player. The enemy's board has its own class because it differs from the player's own board.
 * It differs because this board is simplre. Since it can only know when a ship has been hit or sunk its fields only have three values, regular water, hit water, and hit ship.
 * This class can perform moves on the board and validate a move, meaning check out whether that move hasn't already previously been made.
 * This class is used both when playing multiplayer and signleplayer.
 * TODO: Create an interface to cover the methods that are used both by the GameBoard and the EnemyGameBoard
 */
public class EnemyGameBoard {
    // Possible states of the fields of this board
    public static final String WATER = "WATER";
    public static final String WATER_HIT = "WATER_HIT";
    public static final String SHIP_HIT = "SHIP_HIT";

    // The score of this board
    int score;

    // The actual board
    private String[][] board;

    /**
     * Initialises the board array and then calls {@link #initialiseEmptyBoard(String[][])}
     * to set all fields to water.
     */
    public EnemyGameBoard() {
        board = new String[15][10];
        board = initialiseEmptyBoard(board);
        score = 0;
    }

    /**
     * Used to add score depending on whether a ship and sunk.
     * If true each of them contribute 1 point to the score.
     * @param isHit Indicates whether ship was hit
     * @param isSunk Indicates whether ship was sunk
     */
    public void addScore(boolean isHit, boolean isSunk) {
        if (isHit) {
            score++;
        }
        if (isSunk) {
            score++;
        }
    }

    /**
     * Getter for the score.
     * @return The score of this board.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Getter for the game board created.
     * @return the board.
     */
    public String[][] getBoard() {
        return this.board;
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
            x < GameConstants.BOARD_SIZE_X && 
            y >= 0 && 
            y < GameConstants.BOARD_SIZE_Y && 
            !board[x][y].equals(EnemyGameBoard.SHIP_HIT) &&
            !board[x][y].equals(EnemyGameBoard.WATER_HIT)
        );
    }

    /**
     * Updates enemy board given coordinates of the move and whether or not it was a hit and whether a ship sunk.
     * score are added when ship is hit and when ships sinks.
     * @param x X coordinate of the move.
     * @param y Y coordinate of the move.
     * @param isHit Indicates whether a ships was hit.
     */
    public void makeMove(int x, int y, boolean isHit) {
        if (isHit) {
            board[x][y] = EnemyGameBoard.SHIP_HIT;
        } else {
            board[x][y] = EnemyGameBoard.WATER_HIT;
        }
    }

   /**
     * Initialises a given board by setting all fields of it to the string WATER
     * @param board the board to be initalised with water fields
     * @return the initialised board.
     */
    public String[][] initialiseEmptyBoard(String[][] board) {
        for (int i = 0; i < GameConstants.BOARD_SIZE_X; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE_Y; j++) {
                board[i][j] = EnemyGameBoard.WATER;
            }
        }
        return board;
    }

}
