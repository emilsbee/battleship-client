// External imports
import java.util.Scanner;

public class GameClientTUI {
    // The GameClient instance
    private GameClient client;

    // Re-usable scanner instance for user input
    private Scanner in;

    // The letters to print on the game board
    private static final String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

    
    public GameClientTUI(GameClient client) {
        this.client = client;
        this.in = new Scanner(System.in);
    }

  

    /**
     * Simple method to get an integer input from the user given a certain question.
     * It keeps asking for the integer until a valid one is entered.
     * @param question The question be answered by the user.
     * @return The integer answer to the question.
     */
    public int getInt(String question) {
        int answer;
		System.out.print(question);
		while(true){
			try {
                answer = Integer.parseInt(in.next());
                in.nextLine();
                return answer;
			} catch(NumberFormatException ne) {
				System.out.print("That's not a valid number.\n"+question);
			}
		}
    }

    /**
     * Simple method to get a String input from the user given a certain question.
     * @param question The question to be answered by the user.
     * @return The answer to the question.
     */
    public String getString(String question) {
        
        System.out.print(question);
        return in.nextLine();
        
    }
    
    /**
     * Simple method to get a boolean input from the user given a certain question.
     * It keeps asking for the boolean until a valid one is entered.
     * @param question To be asked to the user
     * @return The boolean answer user provided
     */
    public boolean getBoolean(String question) {
		while(true){
			String input = getString(question);
			if (input.equalsIgnoreCase("yes")) {
				return true;
			} else if (input.equalsIgnoreCase("no")) {
				return false;
			} else {
				showMessage("Please enter yes or no.");
			}
		}
	}

    /**
     * Simple method to more easily display messages in terminal
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
		System.out.println(message);
    }
    /**
     * Printing the score and name of the player just before printing the board.
     * @param score The score of the player that needs to be displayed above the board
     * @param name The name of the player that needs to be displayed above the board
     */
    public void printScore(int score, String name) {
        for(int i = 0; i < 7; i++) {
            printBoardLine("space", 5); // left margin
            if(i == 0 || i == 6) {
                printBoardLine("space", 3);
                printBoardLine("cyan", 29);
                printBoardLine("space", 3);                       
            }
            else if(i == 1 || i  == 5) {
                printBoardLine("space", 2);
                printBoardLine("cyan", 31);
                printBoardLine("space", 2);                       
            }
            else if(i == 2) {
                printBoardLine("space", 1);
                printBoardLine("cyan", 5);
                System.out.print(TerminalColors.BLACK_FONT_BOLD_CYAN_BACKGROUND + "Player name: " + name + TerminalColors.RESET); 
                printBoardLine("cyan", 15 - name.length());  
            }
            else if(i == 3) {
                printBoardLine("cyan", 35);                       
            }
            else if(i == 4) {
                printBoardLine("space", 1);
                printBoardLine("cyan", 5);
                System.out.print(TerminalColors.BLACK_FONT_BOLD_CYAN_BACKGROUND + "Score: " + score + TerminalColors.RESET); 
                printBoardLine("cyan", 21 - String.valueOf(score).length());  
            }
            printBoardLine("newLine", 1);
        }
    }

    /**
     * Prints the given game board in terminal.
     * @param board the board to be printed.
     */
    public void printBoard(String[][] board) {
        // Use this score variable for developing
        int score = 5;
        String name = "Baran";

        /* New lines */
        printBoardLine("newLine", 1);
        /* New lines */

        printScore(score, name);

        /* New lines */
        printBoardLine("newLine", 2);
        /* New lines */

        for (int i = 0; i < 10; i++) {
            
            /* ALPHABET AT THE TOP */
            if (i == 0) {
                System.out.print("      "); // Left margin
                for (int j = 0; j < 15; j++) {
                    printBoardLine("space", 2);
                    System.out.print(alphabet[j].toUpperCase());
                    printBoardLine("space", 3);
                }
                printBoardLine("newLine", 1); // New line
                for (int j = 0; j < 15; j++) {
                    printBoardLine("space", 5);
                }
            }
            /* ALPHABET AT THE TOP */
            

            /* New line */
            printBoardLine("newLine", 1);
            /* New line */

            System.out.print("      "); 
            for (int j = 0; j < 15; j++) { 
                printBoardLine("blue", 5);
                if (j != 14) {   
                    printBoardLine("blue", 1);
                }
            }

            /* New line */
            printBoardLine("newLine", 1);
            /* New line */

            /* Line above the letters */
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    printBoardLine("space", 4);
                }
                if (board[j][i].equals("WATER")) {
                    printBoardLine("blue", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }

                } else {
                    printBoardLine("white", 5);
                    if (client.isShipEnd(j, i)) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                        printBoardLine("white", 1);
                    }
                }
            }
            /* Line above the letters */

            

            /* New line */
            printBoardLine("newLine", 1);
            /* New line */

            /* Line of letters */
            for (int j = 0; j < 15; j++) {

                /* Number on the left */
                if (j == 0) {
                    if (i+1 == 10) {
                        printBoardLine("space", 2);
                        System.out.print(String.valueOf(i+1).toUpperCase());
                        printBoardLine("space", 2);
                    } else {
                        String toPrint = String.valueOf(i+1) + "  "; // This is necessary because every number besides ten takes up one space so it needs to be equaled out
                        printBoardLine("space", 2);
                        System.out.print(toPrint.toUpperCase());
                        printBoardLine("space", 1);
                    }
                }
                /* Number on the left */
                
                /* Actual letter printing W for water and S for ship */
                if (board[j][i].equals("WATER")) {
                    printBoardLine("blue", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
                } else {
                    printBoardLine("white", 2);
                    printBoardLine("ship", 1);
                    printBoardLine("white", 2);
                    if (client.isShipEnd(j, i)) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                        printBoardLine("white", 1);
                    }
                }
                /* Actual letter printing W for water and S for ship */
            }
            /* Line of letters */

            /* New line */
            printBoardLine("newLine", 1);
            /* New line */
            
            /* Line below the letters */
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print("  ");
                    printBoardLine("space", 4);
                }
                if (board[j][i].equals("WATER")) {
                    printBoardLine("blue", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }

                } else {
                    printBoardLine("white", 5);
                    if (client.isShipEnd(j, i)) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                         printBoardLine("white", 1);
                    }
                }
            }
            /* Line below the letters */
    
        }
        /* New line */
        printBoardLine("newLine", 1);
        /* New line */
    }

    /**
     * Prints specific lines for the board a specific amount of times.
     * @param code The code given as a parameter to see what kind of line it needs to be.
     * @param amount the amount of times it needs to be printed.
     */
    public void printBoardLine(String code, int amount){
        for(int i = 0; i < amount; i++){
            switch (code) {
                case "blue":
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                case "white":
                    System.out.print(TerminalColors.WHITE_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                case "ship":
                    System.out.print(TerminalColors.BLACK_FONT_WHITE_BACKGROUND + "S" + TerminalColors.RESET);
                    break;
                case "space":
                    System.out.print(" ");
                    break;
                case "newLine":
                    System.out.println(" ");
                    break;
                case "cyan":
                    System.out.print(TerminalColors.CYAN_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                //case "red":
                    //System.out.print(TerminalColors.RED_BACKGROUND + " "+ TerminalColors.RESET);
                   // break;
            }
       }
    }
}
