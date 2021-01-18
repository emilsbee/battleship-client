import java.io.Serializable;
import java.util.Random;

public class OldGameBoard implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 7688335136468996702L;
	// Ship definitions
    // First item in array is the size and second is amount of ships 
    private static final int[] CARRIER = {5, 2};
    private static final int[] BATTLESHIP = {4, 3};
    private static final int[] DESTROYER = {3, 5};
    private static final int[] SUPER_PATROL = {2, 8}; 
    private static final int[] PATROL_BOAT = {1, 10};

    private static String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};
    Random random;
    String[][] board;


    public OldGameBoard() {
        random = new Random();
        generateBoard();
    }

    public String[][] getBoard() {
        return this.board;
    }

    public void generateBoard() {
        String[][] newBoard = new String[15][10];
        newBoard = initialiseEmptyBoard(newBoard);
        placeShip(OldGameBoard.CARRIER, newBoard);
        placeShip(OldGameBoard.BATTLESHIP, newBoard);
        placeShip(OldGameBoard.DESTROYER, newBoard);
        placeShip(OldGameBoard.SUPER_PATROL, newBoard);
        placeShip(OldGameBoard.PATROL_BOAT, newBoard);
        this.board = newBoard;
        printBoard(newBoard);
    }

    public void placeShip(int[] ship, String[][] board) {
        for (int m = 0; m < ship[1]; m++) {
            boolean fits = false;

            if (ship[0] == 1) { // If the ship is only one field big
                int x = 0;
                int y = 0;
                while (!fits) {
                    x = random.nextInt(alphabet.length);
                    y = random.nextInt(10);
                    fits = looseDoesFit(board, 1, x, y, 0);
                }    

                board[x][y] = "SHIP";
            } else if (ship[0] == 2) { // Two field ships
                int xStart = 0;
                int yStart = 0;
                int direction = 0; // 0: Rightwards, 1: upwards, 2: leftwards, 3: downwards
                
                while (!fits) {
                    xStart = random.nextInt(alphabet.length);
                    yStart = random.nextInt(10);
                    direction = random.nextInt(4);
                    fits = looseDoesFit(board, 2, xStart, yStart, direction);
                }    

                if (direction == 0) { // Rightwards

                    board[xStart][yStart] = "SHIP";
                    board[xStart+1][yStart] = "SHIP";

                } else if (direction == 1) { // Upwards
    
                    board[xStart][yStart] = "SHIP";
                    board[xStart][yStart-1] = "SHIP";
                     
                } else if (direction == 2) { // Leftwards
                    
                    board[xStart][yStart] = "SHIP";
                    board[xStart-1][yStart] = "SHIP";
    
                } else if (direction == 3) { // Downwards
    
                    board[xStart][yStart] = "SHIP";
                    board[xStart][yStart+1] = "SHIP";
    
                }
            } else { // If the ship is more than one field big
                int xStart = 0;
                int yStart = 0;
                int direction = 0; // 0: Rightwards, 1: upwards, 2: leftwards, 3: downwards

                while (!fits) {
                    xStart = random.nextInt(alphabet.length);
                    yStart = random.nextInt(10);
                    direction = random.nextInt(4);

                    fits = doesFit(direction, board, ship[0], xStart, yStart);
                }
                // System.out.println(direction + " X: " +xStart + " Y: " + yStart);
                if (direction == 0) { // If placement is rightwards
                    
                    for (int i = xStart; i <= xStart + (ship[0]-1); i++) {
                        board[i][yStart] = "SHIP";
                    }

                } else if (direction == 1) { // If placement is upwards
                    
                    for (int p = yStart; p >= yStart - (ship[0]-1); p--) {
                        board[xStart][p] = "SHIP";
                    }

                } else if (direction == 2) { // If placement is leftwards

                    for (int k = xStart; k >= xStart - (ship[0]-1); k--) {
                        board[k][yStart] = "SHIP";
                    }

                } else { // If placement is downwards

                    for (int j = yStart; j <= yStart + (ship[0]-1); j++) {
                        board[xStart][j] = "SHIP";
                    }

                }
            }
            // printBoard(board);
        }
    }  

    private boolean looseDoesFit(String[][] board, int shipSize, int x, int y, int direction) {
        if (shipSize == 1) {
            return (
                !board[x][y].equals("SHIP") 
                // !board[x][y+1].equals("SHIP") && 
                // !board[x+1][y].equals("SHIP") && 
                // !board[x-1][y].equals("SHIP") && 
                // !board[x][y-1].equals("SHIP")
            );
        } else if (shipSize == 2) {
            if (direction == 0 && x + (shipSize-1) < 15) { // Rightwards

                return (!board[x][y].equals("SHIP") && !board[x+1][y].equals("SHIP"));

            } else if (direction == 1  && y - (shipSize -1) > -1) { // Upwards

                return (!board[x][y].equals("SHIP") && !board[x][y-1].equals("SHIP"));
                 
            } else if (direction == 2 && x - (shipSize-1) > -1) { // Leftwards
                
                return (!board[x][y].equals("SHIP") && !board[x-1][y].equals("SHIP"));

            } else if (direction == 3 && y + (shipSize-1) < 10) { // Downwards

                return (!board[x][y].equals("SHIP") && !board[x][y+1].equals("SHIP"));

            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private boolean doesFit(int checkDirection, String[][] board, int shipSize, int x, int y) {
        if (checkDirection == 0 && x + (shipSize-1) < 15) { // Checks that ship fits on the board rightwards for x and y given

            return !hasShipAround(board, x, y, y, x+(shipSize-1));
        
        } else if (checkDirection == 1 && y - (shipSize -1) > -1) { // Checks that ship fits on the board upwards for x and y given
        
            int upperMostY = y - (shipSize-1);
            return !hasShipAround(board, x, upperMostY, y, x);
        
        } else if (checkDirection == 2 && x - (shipSize-1) > -1) { // Checks that ship fits on the board leftwards for x and y given
        
            int leftMostX = x - (shipSize-1);
            return !hasShipAround(board, leftMostX, y, y, x);
        
        } else if (checkDirection == 3 && y + (shipSize-1) < 10) { // Checks that ship fits on the board downwards for x and y given
        
            return !hasShipAround(board, x, y, y+(shipSize-1), x);
        
        } else { // If ship doesn't fit on the board
        
            return false;

        }
    }

    private boolean hasShipAround(String[][] board, int x, int y, int shipBottomY, int shipEndX) {
        boolean hasShipAround = false;
        for (int i = (y-1); i <= (shipBottomY+1); i++) { // Iterates over the rows going downwards. Basically from the row above the ship to the row below it

            // If the current row is actually on the board since it could be that the boat is on board but it's on the top edge, so
            // one row above the top edge does not exist, hence does not need to be checked for ships
            if (i >= 0 && i <= 9) { // Row on board

                for (int j = (x-1); j <= (shipEndX+1); j++) {
                    if (j >= 0 && j <= 14 && board[j][i].equals("SHIP")) { // Column on board and the specific field has ship on it already return false
                        hasShipAround = true;
                    }
                }
            } 
        }
        return hasShipAround;
    }

    public String[][] initialiseEmptyBoard(String[][] board) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = "WATER";
            }
        }
        return board;
    }

    public void printBoard(String[][] board) {
        for (int i = 0; i < 10; i++) {
            
            /* ALPHABET AT THE TOP */
            if (i == 0) {
                System.out.print("      "); // Left margin
                for (int j = 0; j < 15; j++) {
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(alphabet[j].toUpperCase());
                    System.out.print(" ");
                    System.out.print(" ");
                }
                System.out.println(" "); // New line
                for (int j = 0; j < 15; j++) {
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                }
            }
            /* ALPHABET AT THE TOP */
            
            /* New line */
            System.out.println(" "); 
            /* New line */

            /* Line above the letters */
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                }
                if (board[j][i].equals("WATER")) {
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND +" "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);

                } else {
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                }
            }
            /* Line above the letters */

            /* New line */
            System.out.println(" "); 
            /* New line */

            /* Line of letters */
            for (int j = 0; j < 15; j++) {

                /* Number on the left */
                if (j == 0) {
                    if (i+1 == 10) {
                        System.out.print(" ");
                        System.out.print(" ");
                        System.out.print(String.valueOf(i+1).toUpperCase());
                        System.out.print(" ");
                        System.out.print(" ");
                    } else {
                        String toPrint = String.valueOf(i+1) + "  "; // This is necessary because every number besides ten takes up one space so it needs to be equaled out
                        System.out.print(" ");
                        System.out.print(" ");
                        System.out.print(toPrint.toUpperCase());
                        System.out.print(" ");
                    }
                }
                /* Number on the left */
                
                /* Actual letter printing W for water and S for ship */
                if (board[j][i].equals("WATER")) {
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + "W" + TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                } else {
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + "S" + TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                }
                /* Actual letter printing W for water and S for ship */
            }
            /* Line of letters */

            /* New line */
            System.out.println(" "); 
            /* New line */
            
            /* Line below the letters */
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                }
                if (board[j][i].equals("WATER")) {
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND +" "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);

                } else {
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                }
            }
            /* Line below the letters */
    
        }
        /* New line */
        System.out.println(" "); 
        /* New line */
    }
}