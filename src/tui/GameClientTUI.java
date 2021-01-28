package tui;

// External imports
import java.util.Scanner;

// Internal imports
import constants.GameConstants;

/**
 * This class is a TUI for the game client. Is prompts questions to the user and displays messages.
 * Also it has methods to print out player's board, the score and name and the enemy's board, score and name.
 * @inv scanner != null
 */
public class GameClientTUI {

    // Re-usable scanner instance for user input
    private Scanner in;

    // The letters to print on the game board
    private static final String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

    /**
     * Initialises the scanner
     */
    public GameClientTUI() {
        this.in = new Scanner(System.in);
    }

    /**
     * Simple method to get an integer input from the user given a certain question.
     * It keeps asking for the integer until a valid one is entered.
     * @param question The question be answered by the user.
     * @return The integer answer to the question.
     * @pre question != null, in != null
     * @post ensures that an actual integer is returned
     */
    public int getInt(String question) {
        int answer;
		showMessage(question);
		while(true){
			try {
                answer = Integer.parseInt(in.next());
                in.nextLine();
                return answer;
			} catch(NumberFormatException ne) {
                showEmptyLines(1);
                showMessageLn(TerminalColors.RED_BOLD+ "> That's not a valid number." + TerminalColors.RESET);
                showEmptyLines(1);
                showMessage(question);
			}
		}
    }

    /**
     * Simple method to get a String input from the user given a certain question.
     * @param question The question to be answered by the user.
     * @return The answer to the question.
     * @pre question != null, in != null
     * @post enusres that a string response is returned 
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
     * @pre question != null, in != null
     * @post ensures that a boolean response is returned
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
     * Requests the game type: multiplayer or singleplayer until the user inputs a valid answer
     * @return The game type player chose m: multiplayer, s: singleplayer.
     * @post ensures that one of the game types is returned m in the case of multiplayer and s in the case of singleplayer
     */
    public String getGameType() {
        String gameType = "";
        boolean correctGameType = false;
		while (!correctGameType) {
			showEmptyLines(1);
			gameType = getString(TerminalColors.PURPLE_BOLD + "> Enter game type you want to play. For multiplayer type m, for singleplayer s: " + TerminalColors.RESET);
			if (gameType.equalsIgnoreCase("m") || gameType.equalsIgnoreCase("s")) {
				correctGameType = true;
			}
        }
        return gameType;
    }

    /**
     * Simple method to more easily display messages in terminal
     * @param message The message to be displayed.
     * @pre message != null
     * @post ensures that a message on the same line is printed
     */
    public void showMessage(String message) {
		System.out.print(message);
    }

    /**
     * Simple method to show some new line message to the user.
     * @param message to show to the user.
     * @pre message != null
     * @post ensures that a message on a new line is printed
     */
    public void showMessageLn(String message) {
        System.out.println(message);
    }

    /**
     * For better formatting and user experience often mutliple empty lines have to be 
     * printed so this method prints as many empty lines as told.
     * @param count The amount of empty lines to be printed.
     * @pre count >= 1
     * @post ensures that the specified amount of empty lines is printed
     */
    public void showEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println(" ");
        }
    }

    /**
     * Printing the score and name of the player just before printing the board.
     * @param score The score of the player that needs to be displayed above the board
     * @param name The name of the player that needs to be displayed above the board
     * @pre score >= 0, name != null, playerType != null
     * @post ensures that the correctly formatted score with name is printed
     */
    public void printScore(int score, String name, String playerType) {
        showEmptyLines(2);
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
                System.out.print(TerminalColors.WHITE_FONT_BOLD_CYAN_BACKGROUND +  playerType + ": " + name + TerminalColors.RESET); 
                printBoardLine("cyan", 26 - playerType.length() - name.length());  
            }
            else if(i == 3) {
                printBoardLine("cyan", 35);                       
            }
            else if(i == 4) {
                printBoardLine("space", 1);
                printBoardLine("cyan", 5);
                System.out.print(TerminalColors.WHITE_FONT_BOLD_CYAN_BACKGROUND + "Score: " + score + TerminalColors.RESET); 
                printBoardLine("cyan", 21 - String.valueOf(score).length());  
            }
            printBoardLine("newLine", 1);
        }
    }

    /**
     * Prints the given game board in terminal.
     * @param board the board to be printed.
     * @pre board != null, score >= 0, name != null
     * @post ensures that a board is correctly printed with the score banner
     */
    public void printBoard(String[][] board, int score, String name) {

        /* New lines */
        printBoardLine("newLine", 1);
        /* New lines */

        printScore(score, name, "Player name");

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

            if (i != 0) {
                System.out.print("      "); 
                for (int j = 0; j < 15; j++) { 
                    printBoardLine("blue", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
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

                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
                } else if (!board[j][i].equals("WATER_HIT") && board[j][i].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
                    printBoardLine("red", 5);
                    if (board[j][i].endsWith("BACK_HIT") || board[j][i].equals("PATROL_HIT")) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                        printBoardLine("red", 1);
                    }
                } else {
                    printBoardLine("white", 5);
                    if (board[j][i].endsWith("BACK") || board[j][i].equals("PATROL")) {
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
                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
                } else if (!board[j][i].equals("WATER_HIT") && board[j][i].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
                    printBoardLine("red", 2);
                    printBoardLine("ship-hit", 1);
                    printBoardLine("red", 2);
                    if (board[j][i].endsWith("BACK_HIT") || board[j][i].equals("PATROL_HIT")) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                        printBoardLine("red", 1);
                    }
                } else {
                    printBoardLine("white", 2);
                    printBoardLine("ship", 1);
                    printBoardLine("white", 2);
                    if (board[j][i].endsWith("BACK") || board[j][i].equals("PATROL")) {
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

                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
                } else if (!board[j][i].equals("WATER_HIT") && board[j][i].endsWith(GameConstants.FIELD_TYPE_HIT_EXTENSION)) {
                    printBoardLine("red", 5);
                    if (board[j][i].endsWith("BACK_HIT") || board[j][i].equals("PATROL_HIT")) {
                        if (j != 14) {   
                            printBoardLine("blue", 1);
                        }
                    } else {
                        printBoardLine("red", 1);
                    }
                } else {
                    printBoardLine("white", 5);
                    if (board[j][i].endsWith("BACK") || board[j][i].equals("PATROL")) {
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
     * Prints enemies board which is more primitive than the user's board since it doesn't have
     * information about exact locations of the ships. So it doesn't include nice spaces between
     * different ships like the user's board does. It doesnt represent ships at all, except when they are hit.
     * @param board The enemy's board
     * @param score The enemy's score
     * @param name The enemy's name
     * @pre board != null, score >= 0, name != null
     * @post ensures that the enemy board is correctly printed with the score banner
     */
    public void printEnemyBoard(String[][] board, int score, String name) {

        /* New lines */
        printBoardLine("newLine", 1);
        /* New lines */

        printScore(score, name, "Enemy name");

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

            if (i != 0) {
                System.out.print("      "); 
                for (int j = 0; j < 15; j++) { 
                    printBoardLine("blue", 5);
                    if (j != 14) {   
                        printBoardLine("blue", 1);
                    }
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

                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("black", 1);
                    }
                } else {
                    printBoardLine("red", 5);
                    if (j != 14) {   
                        printBoardLine("red", 1);
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

                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("black", 1);
                    }
                } else {
                    printBoardLine("red", 5);
                    if (j != 14) {   
                        printBoardLine("red", 1);
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

                } else if (board[j][i].equals("WATER_HIT")) {
                    printBoardLine("black", 5);
                    if (j != 14) {   
                        printBoardLine("black", 1);
                    }
                } else {
                    printBoardLine("red", 5);
                    if (j != 14) {   
                        printBoardLine("red", 1);
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
     * @pre code != null, amount >=0
     * @post ensures that the correct amount and color of lines are printed 
     */
    public void printBoardLine(String code, int amount){
        for(int i = 0; i < amount; i++){
            switch (code) {
                case "black": 
                    System.out.print(TerminalColors.BLACK_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                case "blue":
                    System.out.print(TerminalColors.BLUE_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                case "white":
                    System.out.print(TerminalColors.WHITE_BACKGROUND + " "+ TerminalColors.RESET);
                    break;
                case "ship":
                    System.out.print(TerminalColors.BLACK_FONT_WHITE_BACKGROUND + "S" + TerminalColors.RESET);
                    break;
                case "ship-hit":
                    System.out.print(TerminalColors.BLACK_FONT_RED_BACKGROUND + "S" + TerminalColors.RESET);
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
                case "red":
                    System.out.print(TerminalColors.RED_BACKGROUND + " "+ TerminalColors.RESET);
                   break;
            }
       }
    }
}
