package gameboards.ships;

/**
 * The interface for creating the various ship types.
 */
public interface Ship  {
    /**
     * To get the length of the ship
     * @pre size > 0
     * @post Ensures the size of the ship is returned.
     * @return The lenght of the ship
     */
    public int getSize();


    /**
     * To get the amount of the specific ship that needs to be placed
     * @pre amount > 0
     * @post Ensures the amount of the ship is returned.
     * @return The amount of ships of the implementing type to be placed
     */ 
    public int getAmount();

    /**
     * Given the game board and x and y coordinates, the method will place one ship 
     * rightwards from the given coordinates.
     * @param board The board on which to place the ship
     * @param x X coordinate
     * @param y Y coordinate
     * @pre board != null, x >= 0 && x < 15, y >= 0 && y < 10
     * @post Ensures the ship is placed on given board starting from the given coordinates righwards.
     * @return The instance of this ship. 
     */
    public Ship placeOnBoard(String[][] board, int x, int y);

}