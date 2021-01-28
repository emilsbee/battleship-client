package gameboards.ships;

/**
 * This class represents the super patrols. It contains mostly information about the ship type. It is also used to 
 * place a super patrol on a given board.
 * @inv size >= 0, amount >= 0
 */
public class SuperPatrol implements Ship {
    // The ship size indicates the number of fields it takes up
    private int size;

    // The amount of ships indicates how many this ship needs to be placed on the board
    private int amount; 

    public SuperPatrol() {
        this.size = 2;
        this.amount = 8;
    }

    private enum shipParts {
        SUPER_PATROL_FRONT,
        SUPER_PATROL_BACK
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public int getSize() {
		return this.size;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public int getAmount() {
		return this.amount;
	}

    /**
     * {@inheritDoc}
     */
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
