package ships;


public interface Ship  {
    /**
     * To get the length of the ship
     * @return The lenght of the ship
     */
    public int getSize();


    /**
     * To get the amount of the specific ship that need to be placed
     * @return The amount of ships of the implementing type to be placed
     */ 
    public int getAmount();

    /**
     * Given the game board and x and y coordinates, the method will place one ship 
     * rightwards from the given coordinates.
     * @param board The board on which to place the ship
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Ship placeOnBoard(String[][] board, int x, int y);

}