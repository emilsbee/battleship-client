package gameboards;

// External imports
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Internal imports
import constants.GameConstants;
import gameboards.ships.Battleship;
import gameboards.ships.Carrier;
import gameboards.ships.Destroyer;
import gameboards.ships.Patrol;
import gameboards.ships.Ship;
import gameboards.ships.SuperPatrol;
import protocol.ProtocolMessages;

/**
 * This class represents the game board of a player. It is more detailed than the enemies board since it is known where exactly each ship is displayed. Although 
 * the biggest difference between the two boards is really how they're displayed in the terminal. This class is more detailed about showing the exact location of ships.
 * This board is also used both in multiplayer and singleplayer although there are methods that are specific to the singplayer as they would otherwise be on server side. The javadoc
 * mentions that a method is for singleplayer only. This class keeps track of where each ship is placed and which fields have been shot. It can also encode the board so it could
 * be sent to the server.
 * TODO: Create an interface to cover the methods that are used both by the GameBoard and the EnemyGameBoard
 */
public class GameBoard  {
    // The score of this board
    private int score;

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
        score = 0;
        if (manualPlacement) {
            manualBoard();  
            } else {
            generateBoard();
        }
    }

    /**
     * To manually create the board.
     */
    public void manualBoard() {
        // Implement
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
     * Getter for the score of this board.
     * @return the score of this board.
     */
    public int getScore() {
        return this.score;
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
     * Makes a move if the given field isn't already fired upon.
     * @param x The X coordinate of the move
     * @param y The Y coorindate of the move
     */
    public void makeMove(int x, int y) {
        if (!board[x][y].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
            board[x][y] = board[x][y] + GameConstants.FIELD_TYPE_HIT_EXTENSION;
        }
    }

    /**
     * Given a certain ship type instance, the method finds valid fields on the board to place the ship and then calls
     * {@link #placeOnBoard()} method on the ship instance to place the ship and add it to the ships list. 
     * This method does that for the specific amount times indicated by the ship instance. 
     * @param ship The ship to be placed
     * @param board The board on which the ships are to be placed
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


    /**
     * Encode the String double array board to a regular String in order to send it to the server
     * through sockets.
     * @param board The String[][] board to be encoded.
     * @return A String representation of the String[][] board.
     */
    public String encodeBoard(String[][] board) {
        String encodedBoard = ProtocolMessages.CLIENTBOARD;

        for (int i = 0; i < GameConstants.BOARD_SIZE_Y; i++) {

            for (int j = 0; j < GameConstants.BOARD_SIZE_X; j++) {
                encodedBoard += ProtocolMessages.DELIMITER;
                encodedBoard += board[j][i];
            }

        }

        return encodedBoard;
    }


    /**
     * Called by the game instance to make a move on behalf of the opponent given x and y coordinates of the move.
     * This is only for single player because in multiplayer the server would do this instead.
     * @param x The X coordinate of the move.
     * @param y The Y coordinate of the move.
     * @return Information about whether a ship was hit, whether that hit resulted in sinking the ship, and whether all ships have been destroyed.
     */
    public boolean[] singlePlayerMakeMove(int x, int y) {
        boolean[] update = new boolean[3];
        boolean isHit = false;
        boolean isSunk = false;

        // Checks for whether ship was hit
        if (!board[x][y].equals(GameConstants.FIELD_TYPE_WATER) && !board[x][y].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
            isHit = true;
        }

        // Checks for whether ship was sunk
        if (hasSunk(x, y)) {
            isSunk = true;
        }

        update[0] = isHit;
        update[1] = isSunk;
        update[2] = allShipsDestroyed();
        return update;
    }

    /**
     * Checks whether all ships have been destroyed on this board.
     * This is only for single player because in multiplayer the server would do this instead.
     * @return Whether all ships have been destroyed or not.
     */
    private boolean allShipsDestroyed() {
        boolean allShipsDestroyed = true;

        for (int i = 0; i < GameConstants.BOARD_SIZE_Y; i++) {

            for (int j = 0; j < GameConstants.BOARD_SIZE_X; j++) {

                if (!board[j][i].startsWith(GameConstants.FIELD_TYPE_WATER) && !board[j][i].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
                    allShipsDestroyed = false;
                }
                
            }

        }

        return allShipsDestroyed;
    }

    /**
     * This method actually makes the move on this board and then checks whether it resulted in a ship that sunk.
     * So it also accepts moves on fields that were previously fired upon in which case it just returns false for whether a ship sunk.
     * This is only for single player because in multiplayer the server would do this instead.
     * @param x The X coordinate of the move.
     * @param y The Y coordinate of the move.
     * @return Whether a ship was sunk as a result of the move.
     */
    private boolean hasSunk(int x, int y) {
        boolean hasSunk = true; // Indicator for whether ship was sunk

        String fieldName = board[x][y]; // The name of the field that the move was made upon

        // Since all the field names that are more than one word long are seprated by _ (underscore)
        // this is the array of all the words from the field being moved upon.
        String[] splitFieldName = board[x][y].split("_"); 

        // The first word in the field being moved upon. This indicates what kind of field and what kind of ship it is.
        String fieldNameBeginning = splitFieldName[0];

        // All ships are placed horizontally and the ships start is on the left side. So they are placed from left to right.
        // So to check whether a ships has sunk, the start coordinate, x1, has to be found and the end coordinate, x2,
        // has to be found. So these two variables represent that.
        int x1; 
        int x2;

        if (!fieldName.endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) { // If the field wasn't previously fired on

            board[x][y] = fieldName+GameConstants.FIELD_TYPE_HIT_EXTENSION; // Adding the hit extension to whatever field this previously was
        
            if (fieldNameBeginning.equals(GameConstants.FIELD_TYPE_PATROL)) { // If the field is a patrol ship

                x1 = x;
                x2 = x;

            } else if (fieldName.startsWith("SUPER_PATROL") && !fieldName.endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) { // If the field is super patrol ship
                
                if (fieldName.endsWith("FRONT")) {
                    x1 = x;
                    x2 = x+1;
                } else {
                    x1 = x-1;
                    x2 = x;
                }
    
            } else if (fieldName.startsWith("DESTROYER") && !fieldName.endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) { // If the field is destroyer ship
    
                if (fieldName.endsWith("FRONT")) {
                    x1 = x;
                    x2 = x+2;
                } else if (fieldName.endsWith("MID")) {
                    x1 = x-1;
                    x2 = x+1;
                } else {
                    x1 = x-2;
                    x2 = x;
                }
    
            } else if (fieldName.startsWith("BATTLESHIP") && !fieldName.endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) { // If the field is battleship
    
                if (fieldName.endsWith("FRONT")) {
                    x1 = x;
                    x2 = x+3;
                } else if (fieldName.endsWith("FRONT_MID")) {
                    x1 = x-1;
                    x2 = x+2;
                } else if (fieldName.endsWith("BACK_MID")) {
                    x1 = x-2;
                    x2 = x+1;
                } else {
                    x1 = x-3;
                    x2 = x;
                }
    
            } else if (fieldName.startsWith("CARRIER") && !fieldName.endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) { // If the field is carrier
    
                if (fieldName.endsWith("FRONT")) {
                    x1 = x;
                    x2 = x+4;
                } else if (fieldName.endsWith("FRONT_MID")) {
                    x1 = x-1;
                    x2 = x+3;
                } else if (fieldName.endsWith("MID")) {
                    x1 = x-2;
                    x2 = x+2;
                } else if (fieldName.endsWith("BACK_MID")) {
                    x1 = x-3;
                    x2 = x+1;
                } else {
                    x1 = x-4;
                    x2 = x;
                }
    
            } else { // If the field is water
                return false;
            }
    
            // The loop that iterates from start of the ship to the end to check whether all parts of it are hit.
            for (int i = x1; i <= x2; i++) {
                if (!board[i][y].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
                    hasSunk = false;
                }
    
            }

        } else { // If this field was previously fired upon already

            hasSunk = false;
        
        }
        
        return hasSunk;
    }
}


