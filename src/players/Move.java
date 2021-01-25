package players;

// External imports
import java.util.Scanner;

// Internal imports
import game.Game;
import exceptions.ServerUnavailableException;
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
    private HumanPlayer player;

    // The terminal view
    private GameClientTUI view;

    // The game instance
    private Game game;

    // Initialises the move 
    public Move(EnemyGameBoard enemyBoard, HumanPlayer player, GameClientTUI view, Game game) {
        this.enemyBoard = enemyBoard;
        this.player = player;
        this.view = view;
        this.game = game;
    }

    /**
     * Prompts user to make a move until a valid move is entered. 
     * This prompt is always active after game has begun so even if it's not your
     * move you can enter coordinats. However, the move won't be sent to the server
     * because myMove variable in client is always updated about whose move it is.
     * If it user's move and user enters valid coordinates, then the move method is called on client. 
     * @throws ServerUnavailableException
     */
    public void getMove()  {
        boolean validMove = false; // Indicator for whether a move is valid

        String xChar; // The X coordinate enter by the user as a string
        int x = 0; // The actual X coordinate sent to the server
        int y = 0; // The Y corrdinate entered by user and sent to server
        
		while (!validMove) {
            
            // view.showEmptyLines(1);
            String coordinates = getString("");

            if (coordinates.equals("q")) { // If user indicates to exit
                System.exit(0);
            }

            if (game.hasGameEnded()) { // If game has already ended
                
                view.showEmptyLines(1);
                view.showMessage("> Enter q to exit game: ");

            } else { 

                if (!game.isHumanPlayersMove()) { // If it's not the user's turn

                    view.showEmptyLines(1);   
                    view.showMessageLn(TerminalColors.RED_BOLD + "> Not your turn." + TerminalColors.RESET);
                    view.showEmptyLines(1);
                    view.showMessage("> Enter coordinates: ");

                } else { // If it is the user's turn

                    try {
                        // Parses the entered coordinates
                        xChar = coordinates.split(",")[0];
                        y = Integer.parseInt(coordinates.split(",")[1]);
                        
                        // Checks whether the character that user inputs is a valid one in terms of whether it's on the board
                        if (indexOfLetterInAlphabet(xChar) != -1) {  // If the user entered X coordinate is valid
                            
                            validMove = enemyBoard.isValidMove(indexOfLetterInAlphabet(xChar), y-1); 
                        
                        } else { // If the user entered X coordinate is invalid
                        
                            validMove = false;
                        
                        }
            
                       if (!validMove) { // If the move input is invalid
                        
                        view.showEmptyLines(1);
                            view.showMessageLn(TerminalColors.RED_BOLD + "> Invalid move." + TerminalColors.RESET);
                            view.showEmptyLines(1);
                            view.showMessage("> Enter coordinates: ");
                        
                        } else {

                            x = indexOfLetterInAlphabet(xChar);
                            player.makeMove(x, y-1);

                        }
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException | StringIndexOutOfBoundsException e) {
                        view.showEmptyLines(1);
                        view.showMessageLn(TerminalColors.RED_BOLD + "> Input is invalid. Remember input format example: a,2. Or type q to exit."+ TerminalColors.RESET);
                        view.showEmptyLines(1);
                        view.showMessage("> Enter coordinates: ");
                    }
                }
            }
         }
    }

    /**
     * Get the x coordinate as number from the letter that user inputs
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
            getMove();
            
        }
		
	}


}
