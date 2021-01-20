package gameboard;

// External imports
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Internal imports
import ships.*;


public class GameBoard  {

    // List of ships
    private List<Ship> ships;

    // The game board
    private String[][] board;

    // Re-usable instance of Random
    Random random;
    
    /**
     * Constructor that calls for a board creation based on the argument. Either 
     * calls to randomly generate the board or manually.
     * @param manualPlacement Indicates whether the board will be created manually or randomly.
     */
	public GameBoard(boolean manualPlacement) {
        random = new Random();
        ships = new ArrayList<>();
        if (manualPlacement) {
            manualBoard();  
        } else {
            generateBoard();
        }
    }

    public void manualBoard() {
        // Some comment
    }

    /**
     * Initialises, generates and sets a randomly created board. 
     */
    public void generateBoard() {
        String[][] newBoard = new String[15][10];
        newBoard = initialiseEmptyBoard(newBoard);
        findPlaceOnBoard(new Carrier(), newBoard);
        findPlaceOnBoard(new Battleship(), newBoard);
        findPlaceOnBoard(new Destroyer(), newBoard);
        findPlaceOnBoard(new SuperPatrol(), newBoard);
        findPlaceOnBoard(new Patrol(), newBoard);

        setBoard(newBoard);
    }

    /**
     * Setter for a newly created board.
     * @param board the board to be set.
     */
    public void setBoard(String[][] board) {
        this.board = board;
    }
    

    /**
     * Getter for the game board created.
     * @return the board.
     */
    public String[][] getBoard() {
        return this.board;
    }

    /**
     * Given a certain ship type instance, the method finds valid fields on the board to place the ship and then calls
     * {@link #placeOnBoard(String[][], int, int)} method on the ship instance to place the ship and add it to the ships list. 
     * This method does that for the specific amount of the ship needed. 
     * @param ship
     * @param board
     */
    public void findPlaceOnBoard(Ship ship, String[][] board) {
        for (int shipCount = 0; shipCount < ship.getAmount(); shipCount++) { // Iterates over the number of ships
            
            /* Find free fields to place the ship */
            boolean isPlaced = false;
            int x = 0;
            int y = 0;
            while (!isPlaced) {
                
                x = random.nextInt(15);
                y = random.nextInt(10);

                isPlaced = doesFit(x, y, ship.getSize(), board);
            }
            
            ships.add(ship.placeOnBoard(board, x, y));
        }
    }

    /**
     * Checks whether a given ship size fits on board and whether there are no other ships that the ship would overlap.
     * @param x Random X coordinate 
     * @param y Random Y coordinate
     * @param shipSize The size of the ship to be checked
     * @param board The game board
     * @return Whether the ship fits on the board and doesn't overlap any other ships
     */
    public boolean doesFit(int x, int y, int shipSize, String[][] board) {
        if (x + (shipSize-1) < 15) { // Checks whether ship fits on board

            for (int i = x; i < x+shipSize; i++) { // Iterates over the fields that the ship would take up
                if (!board[i][y].equals("WATER")) {
                    return false;
                } 
            }
            return true;
        } 

        return false;
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


