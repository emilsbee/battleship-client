package client;

// External imports
import java.util.Scanner;

// Internal imports
import gameboard.EnemyGameBoard;
import tui.TerminalColors;

public class Move implements Runnable {
    // To convert the char input to an integer
    private static final char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'};


    // Scanner variable to get user input
    Scanner scanner;

    // Enemies game board
    EnemyGameBoard enemyBoard;
    
    // The client
    GameClient client;

    public Move(EnemyGameBoard enemyGameBoard, GameClient client) {
        this.enemyBoard = enemyGameBoard;
        this.client = client;
    }

    /**
     * Prompts user to make a move until a valid move is entered. 
     * This prompt is always active after game has begun so even if it's not your
     * move you can enter coordinats. However, the move won't be sent to the server
     * because myMove variable in client is always updated about whose move it is.
     * If it user's move and user enters valid coordinates, then the move method is called on client. 
     */
    public void getMove() {
        boolean validMove = false;
        char xChar;
        int x = 0;
		int y = 0;
		while (!validMove) {
            System.out.println(" ");
            String coordinates = getString("> Enter coordinates: ");

            try {
                xChar = coordinates.split(",")[0].charAt(0);
                y = Integer.parseInt(coordinates.split(",")[1]);
                
                if (indexOfLetterInAlphabet(xChar) != -1 && coordinates.split(",")[0].length() == 1) { // Checks whether the character that user inputs is a valid one in terms of whether it's on the board
                    validMove = enemyBoard.isValidMove(indexOfLetterInAlphabet(xChar), y-1); 
                } else {
                    validMove = false;
                }
    
                if (!validMove && !client.getMyMove()) { // Checks whether the move entered is valid and whether it's the user's move
                    showMessageLn(" ");      
                    showMessageLn(TerminalColors.RED_BOLD + "> Invalid move and not your turn." + TerminalColors.RESET);
                
                } else if (!validMove) { // If it is the user's move but the move input is invalid
                    showMessageLn(" ");
                    showMessageLn(TerminalColors.RED_BOLD + "> Invalid move." + TerminalColors.RESET);
                }

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | StringIndexOutOfBoundsException e) {
                showMessageLn(" ");
                showMessageLn(TerminalColors.RED_BOLD + "> Input is invalid. Remember input format example: a,2"+ TerminalColors.RESET);

            }

            
         }
        client.move(x, y-1);
    }

    /**
     * Get the x coordinate as number from the letter that user inputs
     * @param letter the letter to check 
     * @return -1 if the letter isn't a valid input for a move, else the respective x value on the board
     */
    public int indexOfLetterInAlphabet(char letter) {
        
        for (int i = 0; i < alphabet.length; i++) {
            if (String.valueOf(alphabet[i]).equals(String.valueOf(letter))) {
                return i;
            }
        }
        return -1;
     }


    /**
     * Promopts user the question for coordinates.
     * @param question The question for user to answer.
     * @return User's input.
     */
    public String getString(String question) {
        scanner = new Scanner(System.in);
        showMessage(TerminalColors.PURPLE_BOLD+ question+ TerminalColors.RESET);
        return scanner.nextLine();
        
    }

    /**
     * Simple method to show some new line message to the user.
     * @param message to show to the user.
     */
    public void showMessageLn(String message) {
        System.out.println(message);
    }

    /**
     * Simple method to show some same line message to the user.
     * @param message message to show to the user
     */
    public void showMessage(String message) {
        System.out.print(message);
    }

	@Override
	public void run() {
        
        while (true) {
            getMove();
        }
		
	}


}
