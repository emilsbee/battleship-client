package game;

// External imports
import java.util.Random;
import java.util.concurrent.TimeUnit;

// Internal imports
import players.ComputerPlayer;
import players.HumanPlayer;                                                                                                                                          
import tui.GameClientTUI;
import tui.TerminalColors;

public class Game implements Runnable {
    // The constants to tell apart the human and computer player
    static final String HUMAN_PLAYER = "human";
    static final String COMPUTER_PLAYER = "computer";

    // Human and computer player instances through which moves
    // are demanded and boards updated.
    private HumanPlayer humanPlayer;
    private ComputerPlayer computerPlayer;

    // The points of each player
    private int humanPlayerPoints;
    private int computerPlayerPoints;
    
    // Indicates whos move it currently is
    private String currentMove;

    // Re-usable instance of random
    Random random;

    // The terminal view to display messages and show prompts to user
    private GameClientTUI view;

    // Indicates whether the game has ended
    private boolean gameEnded;

    // The game loop thread
    private Thread gameThread;

    /**
     * Constructs a new game by creating both human and computer players, initialising score and then
     * calling {@link #startGame()} method to start the game. 
     * @param playerName The name of the player that will play against the computer.
     * @param view The TUI.
     */
    public Game(String playerName, GameClientTUI view) {
        humanPlayer = new HumanPlayer(playerName, this, view);
        computerPlayer = new ComputerPlayer(this);
        random = new Random();
        this.view = view;
        humanPlayerPoints = 0;
        computerPlayerPoints = 0;
        gameEnded = false;
        startGame();
    }


    // The game thread loop that is stuck in a while loop until 5 minutes are up. 
    // It iterates every 1 second since there is no need
    // to be accurace to milliseconds. If the game is finished because 5 minutes are up
    // meaning thread wasn't interrupted, then the thread informs the human player about what the end result is.
    @Override
    public void run() {
        decideWhoStarts(); // Randomly chooses who goes first and sets the currentMove variable accordingly

        long t = System.currentTimeMillis();  
        long end = t + 300000; // Ends in 5 minutes

        while(System.currentTimeMillis() < end && !gameThread.isInterrupted()) {
   
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                gameThread.interrupt();
                break;
            }

        }

        if (!gameThread.isInterrupted()) {
            if (humanPlayerPoints > computerPlayerPoints) {
                humanPlayer.gameOver(0);
            } else if (humanPlayerPoints < computerPlayerPoints) {
                humanPlayer.gameOver(1);
            } else {
                humanPlayer.gameOver(2);
            }
        }

        gameEnded = true;
    }

    /**
     * Starts the game loop thread.
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Called by one of the players when they make a move. Or in the case of human player
     * the timer can call this and inform that the move was made late. This method when called 
     * informs both players about the move made and the result of that move. It also keeps track of 
     * each players points. If the results after a move indicate that all ships have been destroyed it stops 
     * the game by interrupting the game loop thread and informs both players about the result of the game.
     * Also, after each move it requests the respective player to make a move.
     */
    public void makeMove(int x, int y, boolean isLate) {
        if (!gameEnded) {

            if (currentMove.equals(Game.COMPUTER_PLAYER)) { // Enemies move
                
                if (isLate) { // If enemy made a late move
                    currentMove = Game.HUMAN_PLAYER;
                    humanPlayer.getMove();
                }
    
                else { // If enemy made a move on time
    
                    // Asking the human player whether the move hit and sunk a ship
                    // result[0]: isHit, result[1]: isSunk, result[2]: areAllShipsDestroyed
                    // the human player also updates its own board with this move
                    boolean[] result = humanPlayer.enemyMove(x, y); 
    
                    computerPlayer.update(x, y, result[0]); // Update the computer's enemy board with the results
        
                    if (result[2]) { // If enemy destroyed all ships
                        gameThread.interrupt();
                        humanPlayer.gameOver(1);
                        gameEnded = true;
    
                    } else { // If all ships weren't destroyed
    
                        if (result[0]) { // If ship was hit
    
                            if (result[1]) { // If ship was sunk
    
                                computerPlayerPoints++;
                            
                            }
    
                            computerPlayerPoints++;
                            humanPlayer.printBoards("Enemy hit your ship.", TerminalColors.RED_BOLD);
                            computerPlayer.getMove();
                        
                        } else { // If no ship was hit
    
                            humanPlayer.printBoards("Enemy missed your ship, your move!", TerminalColors.GREEN_BOLD);
                            currentMove = Game.HUMAN_PLAYER;
                            humanPlayer.getMove();
                        
                        }
            
                    }
                }
    
                
            } else { // Human player's move
    
                if (isLate) { // If the human player's move was made late
    
                    view.showEmptyLines(2);
                    view.showMessageLn(TerminalColors.RED_BOLD + "You missed your move!" + TerminalColors.RESET);
                    view.showEmptyLines(1);
                    view.showMessageLn(TerminalColors.PURPLE_BOLD + "Enter coordinates: " + TerminalColors.RESET);
                    currentMove = Game.COMPUTER_PLAYER;
                    computerPlayer.getMove();
                
                } else { // If the human player's move was made on time
    
                    // Asking the computer player whether the move hit and sunk a ship
                    // result[0]: isHit, result[1]: isSunk, result[2]: areAllShipsDestroyed
                    // the computer player also updates its own board with this move
                    boolean[] result = computerPlayer.enemyMove(x, y);
    
                    humanPlayer.update(x, y, result[0]); // Update the humans's enemy board with the results
    
                    if (result[2]) { // If all ships destroyed
                        gameThread.interrupt();
                        humanPlayer.gameOver(0);
                        gameEnded = true;
    
                    } else { // If all ships are not yet destroyed
            
                        if (result[0]) { // If a ship was hit
                            humanPlayerPoints++;
    
                            if (result[1]) { // If ship was sunk
                                humanPlayerPoints++;
                                humanPlayer.printBoards("You sunk enemies ship! Shoot again!", TerminalColors.GREEN_BOLD);
                            } else {
                                humanPlayer.printBoards("You hit enemies ship! Shoot again!", TerminalColors.GREEN_BOLD);
                            }
                            humanPlayer.getMove();
                        } else { // If no ships were hit
    
                            humanPlayer.printBoards("You missed enemies ship! Enemies move now.", TerminalColors.RED_BOLD );
                            currentMove = Game.COMPUTER_PLAYER;
                            computerPlayer.getMove();
                        }
            
                        
                    }
                }
    
            }
        } 

    }

    /**
     * @return Whether the game has ended.
     */
    public boolean hasGameEnded() {
        return gameEnded;
    }

    /**
     * Randomly decides which player should start and requests a move from that player.
     */
    public void decideWhoStarts() {
        int whoStarts = random.nextInt(2);
        if (whoStarts == 1) {
            currentMove = Game.COMPUTER_PLAYER;
            computerPlayer.getMove();
        } else {
            currentMove = Game.HUMAN_PLAYER;
            humanPlayer.getMove();
            humanPlayer.printBoards("You're move!", TerminalColors.GREEN_BOLD);
        }
    }


    /**
     * Getter for how many point the human player has.
     * @return The amount of points that human player has.
     */
    public int getHumanPlayerPoints() {
        return this.humanPlayerPoints;
    }

    /**
     * Getter for how many point the computer player has.
     * @return The amount of points that computer player has.
     */
    public int getComputerPlayerPoints() {
        return this.computerPlayerPoints;
    }

    /**
     * To check whether it is the human players move
     * @return Whether it is the human players move.
     */
    public boolean isHumanPlayersMove() {
        return currentMove.equals(Game.HUMAN_PLAYER);
    }
}
