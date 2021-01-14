import java.util.Arrays;

public class GameBoard {
    // Ship definitions
    // First item in array is the size and second is amount of ships 
    private static final int[] CARRIER = {5, 2};
    private static final int[] BATTLESHIP = {4, 3};
    private static final int[] DESTROYER = {3, 5};
    private static final int[] SUPER_PATROL = {2, 8};
    private static final int[] PATROL_BOAT = {1, 10};

    private static String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

    public String[][] generateBoard() {
        String[][] board = new String[10][15];
        board = initialiseBoard(board);
        printBoard(board);

        return board;
    }

    public void placeShip(int[] ship, String[][] board) {
        //comment
    }

    public int[] getFreeFields(String[][] board) {
        return null;
    }

    public String[][] initialiseBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "WATER";
            }
        }
        return board;
    }

    public void printBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            
            /* ALPHABET AT THE TOP */
            if (i == 0) {
                System.out.print("      "); // Left margin
                for (int j = 0; j < board[i].length; j++) {
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(alphabet[j]);
                    System.out.print(" ");
                    System.out.print(" ");
                }
                System.out.println(" "); // New line
                for (int j = 0; j < board[i].length; j++) {
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
            for (int j = 0; j < board[i].length; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                }
                if (board[i][j].equals("WATER")) {
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
            for (int j = 0; j < board[i].length; j++) {

                /* Number on the left */
                if (j == 0) {
                    if (i+1 == 10) {
                        System.out.print(" ");
                        System.out.print(" ");
                        System.out.print(String.valueOf(i+1));
                        System.out.print(" ");
                        System.out.print(" ");
                    } else {
                        String toPrint = String.valueOf(i+1) + "  "; // This is necessary because every number besides ten takes up one space so it needs to be equaled out
                        System.out.print(" ");
                        System.out.print(" ");
                        System.out.print(toPrint);
                        System.out.print(" ");
                    }
                }
                /* Number on the left */
                
                /* Actual letter printing W for water and S for ship */
                if (board[i][j].equals("WATER")) {
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + "W" + TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                } else {
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.GREEN_BACKGROUND + " "+ TerminalColors.RESET);
                    System.out.print(TerminalColors.BLUE_BACKGROUND + "S" + TerminalColors.RESET);
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
            for (int j = 0; j < board[i].length; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                    System.out.print(" ");
                }
                if (board[i][j].equals("WATER")) {
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
    }
}
