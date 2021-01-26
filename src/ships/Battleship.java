package ships;

/**
 * This class represents the battleships. It contains mostly information about the ship type. It is also used to 
 * place a battleship on a given board.
 */
public class Battleship implements Ship {
    // The ship size indicates the number of fields it takes up
    private int size;

    // The amount of ships indicates how many this ship needs to be placed on the board
    private int amount; 


    public Battleship() {
        this.size = 4;
        this.amount = 3;
    }

    private enum shipParts {
        BATTLESHIP_FRONT,
        BATTLESHIP_FRONT_MID,
        BATTLESHIP_BACK_MID,
        BATTLESHIP_BACK
    }


	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

	@Override
	public Ship placeOnBoard(String[][] board, int x, int y) {
 

        /* Place the ship on board */
        int count = 0;
        for (int xPos = x; xPos < x + this.getSize(); xPos++) {
            board[xPos][y] = shipParts.values()[count].toString();
            count++;
        } 

        return this;
	}
    
}
