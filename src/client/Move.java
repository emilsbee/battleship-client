package client;

// External imports
import java.util.Scanner;

// Internal imports
import exceptions.ServerUnavailableException;
import gameboard.EnemyGameBoard;
import tui.GameClientTUI;
import tui.TerminalColors;

/**
 * This classes is used by the game client to continuously ask for user input for a move. This promopting for user input is on its own
 * thread so as to not block the showing of other messages in terminal. Just because this always prompts for a move, doesn't mean that
 * the move can always be made. That's because the game client keeps track of whether it is the user's move or not. Also, this continuously 
 * prompting thread for user input allows the user to exit the game at any point by typing q. Ultimately, this idea for a thread that always asks
 * for a move came about because of the 30 second restriction on a move. If a user misses a move then the scanner.readLine() should be canceled, but
 * it can't be. Hence, it just always asks for input. Important to note that this thread is only started once a client has succesfully connected to the server
 * and a game has begun
 * TODO: Converge this class with the one in players package. 
 */
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
     * because myMove variable in client is always updated about whos move it is.
     * If it user's move and user enters valid coordinates, then the move method is called on client. 
     * @throws ServerUnavailableException
     */
    public void getMove() throws ServerUnavailableException {

        boolean validMove = false; // Indicator for whether a move is valid
        
        String xChar; // The X coordinate enter by the user as a string
        int x = 0; // The actual X coordinate sent to the server
		int y = 0; // The Y corrdinate entered by user and sent to server
        
        while (!validMove) {
            
            view.showEmptyLines(1);
            String coordinates = getString("> Enter coordinates or q to quit: ");

            if (coordinates.equals("q")) { // If user indicates to exit
                client.sendExit();
                System.exit(0);
            }

            if (!client.getMyMove()) {  // If it's not the user's move

                view.showEmptyLines(1);   
                view.showMessageLn(TerminalColors.RED_BOLD + "> Not your move." + TerminalColors.RESET);
        
            } else { // If it is the user's move

                try {
                    // Parses the entered coordinates
                    xChar = coordinates.split(",")[0];
                    y = Integer.parseInt(coordinates.split(",")[1]);
                    
                    // Checks whether the character that user inputs is a valid one in terms of whether it's on the board
                    if (indexOfLetterInAlphabet(xChar) != -1) {  // If the user entered X coordinate is valid
    
                        // Checks whether user didn't previously fire in that location already
                        validMove = enemyBoard.isValidMove(indexOfLetterInAlphabet(xChar), y-1);  
                    
                    } else { // If the user entered X coordinate is invalid
    
                        validMove = false;
                    
                    }
        
                    if (!validMove) { // If the move is invalid
                  
                        view.showEmptyLines(1);
                        view.showMessageLn(TerminalColors.RED_BOLD + "> Invalid move." + TerminalColors.RESET);
                    
                    } else { // If the move is valid

                        x = indexOfLetterInAlphabet(xChar); // Gets the actual X coordinate from the character entered by user
                        client.move(x, y-1); // Make the move
                    }
    
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | StringIndexOutOfBoundsException e) {
                    view.showEmptyLines(1);
                    view.showMessageLn(TerminalColors.RED_BOLD + "> Input is invalid. Remember input format example: a,2"+ TerminalColors.RESET);
    
                }
                
            }
         }
    }


    /**
     * Get the x coordinate as number from the letter that user inputs.
     * @param letter the letter to check 
     * @return -1 if the letter isn't a valid input for a move, else the respective x value on the board
     */
    public int indexOfLetterInAlphabet(String letter) {
        if (letter.length() != 1) { 

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

    /**
     * The thread loop that keeps asking for user input until user indicates to quit.
     */
	@Override
	public void run() {
        
        while (true) {
            try {
				getMove();
			} catch (ServerUnavailableException e) {
                view.showMessage("Server unavailable.");
                System.exit(0);
			}
        }
		
	}


}
