package ships;

// External imports
import java.awt.Point;


public class SuperPatrol implements Ship {
    private int size = 2;
    private int amount = 8; 
    private Point[] position;

    private enum shipParts {
        SUPER_PATROL_FRONT,
        SUPER_PATROL_BACK
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
        // Initialises the position array
        position = new Point[this.getSize()];

        /* Place the ship on board */
        int count = 0;
        for (int xPos = x; xPos < x + this.getSize(); xPos++) {
            board[xPos][y] = shipParts.values()[count].toString();
            position[count] = new Point(xPos, y); 
            count++;
        } 

        return this;
	}
    
}
