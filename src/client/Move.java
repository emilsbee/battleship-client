package client;

// External imports
import java.util.Scanner;

// Internal imports
import gameboard.EnemyGameBoard;
import tui.GameClientTUI;
import tui.TerminalColors;

public class Move implements Runnable {
    // To convert the char input to an integer
    private static final String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};


    // Scanner variable to get user input
    Scanner scanner;

    // Enemies game board
    private EnemyGameBoard enemyBoard;
    
    // The client
    private GameClient client;

    // The terminal view
    private GameClientTUI view;

    public Move(EnemyGameBoard enemyBoard, GameClient client, GameClientTUI view) {
        this.enemyBoard = enemyBoard;
        this.client = client;
        this.view = view;
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
        String xChar;
        int x = 0;
		int y = 0;
		while (!validMove) {
            view.showEmptyLines(1);
            String coordinates = getString("> Enter coordinates: ");

            if (coordinates.equals("q")) {
                System.exit(0);
            }

            try {
                xChar = coordinates.split(",")[0];
                y = Integer.parseInt(coordinates.split(",")[1]);
                
                if (indexOfLetterInAlphabet(xChar) != -1 && coordinates.split(",")[0].length() == 1) { // Checks whether the character that user inputs is a valid one in terms of whether it's on the board
                    validMove = enemyBoard.isValidMove(indexOfLetterInAlphabet(xChar), y-1); 
                } else {
                    validMove = false;
                }
    
                if (!validMove && !client.getMyMove()) { // Checks whether the move entered is valid and whether it's the user's move
                    view.showEmptyLines(1);   
                    view.showMessageLn(TerminalColors.RED_BOLD + "> Invalid move and not your turn." + TerminalColors.RESET);
                
                } else if (!validMove) { // If it is the user's move but the move input is invalid
                    view.showEmptyLines(1);
                    view.showMessageLn(TerminalColors.RED_BOLD + "> Invalid move." + TerminalColors.RESET);
                }

                if (validMove) {
                    x = indexOfLetterInAlphabet(xChar);
                }

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | StringIndexOutOfBoundsException e) {
                view.showEmptyLines(1);
                view.showMessageLn(TerminalColors.RED_BOLD + "> Input is invalid. Remember input format example: a,2"+ TerminalColors.RESET);

            }

            
         }

        client.move(x, y-1);
    }

    /**
     * Get the x coordinate as number from the letter that user inputs
     * @param letter the letter to check 
     * @return -1 if the letter isn't a valid input for a move, else the respective x value on the board
     */
    public int indexOfLetterInAlphabet(String letter) {
        if (letter.length() > 1 || letter.length() <= 0) {
            return -1;
        } else {
            for (int i = 0; i < alphabet.length; i++) {
                if (alphabet[i].equals(letter)) {
                    return i;
                }
            }
            return -1;
        }
     }


    /**
     * Promopts user the question for coordinates.
     * @param question The question for user to answer.
     * @return User's input.
     */
    public String getString(String question) {
        scanner = new Scanner(System.in);
        view.showMessage(TerminalColors.PURPLE_BOLD+ question+ TerminalColors.RESET);
        return scanner.nextLine();
        
    }

	@Override
	public void run() {
        
        while (true) {
            getMove();
        }
		
	}


}
