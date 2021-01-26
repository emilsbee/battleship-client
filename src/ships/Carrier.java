package ships;

/**
 * This class represents the carriers. It contains mostly information about the ship type. It is also used to 
 * place a carrier on a given board.
 */
public class Carrier implements Ship {
    // The ship size indicates the number of fields it takes up
    private int size;

    // The amount of ships indicates how many this ship needs to be placed on the board
    private int amount; 
    
    public Carrier() {
        this.size = 5;
        this.amount = 2;
    }

    private enum shipParts {
        CARRIER_FRONT,
        CARRIER_FRONT_MID,
        CARRIER_MID,
        CARRIER_BACK_MID,
        CARRIER_BACK
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
