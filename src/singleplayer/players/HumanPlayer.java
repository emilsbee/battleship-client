package singleplayer.players;

// External imports
import java.util.Timer;
import java.util.TimerTask;

import gameboards.EnemyGameBoard;
import gameboards.GameBoard;
import singleplayer.game.Game;
import tui.GameClientTUI;
import tui.TerminalColors;

/**
 * This class represents the human player in a singplayer game. It manages the human players
 * game board and the move thread. As well as creates timer for 30 second in which the user must
 * perform the move, otherwise the timer will perform the move and mark it as a late move. This class
 * is also responsible for showing messages about the status of the game to the user.
 * @inv playerName != null, game != null, board != null, view != null, enemyBoard != null, moveObj != null, moveThread != null
 */
public class HumanPlayer implements Player  {
    // The player name of the human player
    private String playerName;

    // The game instance
    private Game game;

    // Re-usable task variable that is used for player move timer
    private TimerTask task;
    
    // Re-usable timer variable for scheduling the player move timer task
    private Timer timer; 

    // The board of human player
    private GameBoard board;

    // The TUI
    private GameClientTUI view;
    
    // The enemies board where to keep track of human player's moves
    private EnemyGameBoard enemyBoard;

    // The move class that is put in a thread to get user input for moves
	private Move moveObj;

	// The thread in which moveObj is put in
	private Thread moveThread;


    /**
     * Initialises a new human player
     * @param playerName The name of the human player
     * @param game The game instance
     * @param view The TUI
     * @pre playerName != null, game != null, view != null
     * @post ensures that playerName, game, view, board, enemyBoard, moveObj, moveThread are initialised
     * and that moveThread is started.
     */
    public HumanPlayer(String playerName, Game game, GameClientTUI view) {
        this.playerName = playerName;
        this.game = game;
        this.view = view;
        this.board = new GameBoard(false);
        this.enemyBoard = new EnemyGameBoard();
        moveObj = new Move(enemyBoard, this, view, game);
        moveThread = new Thread(moveObj);
        moveThread.start();
    }
    
    /**
     * This method is called by the game when it's the human player's move. It creates a 30 second timer and if
     * the user doesn't make their move in time, then this timer makes the 
     * move for them. The move is marked as late so even though it includes valid
     * coordinates, they are not actually taken into account by the game.
     * @pre game != null
     * @post ensures that a timer is started for 30 second and that after which 
     * a move marked as late is made on behalf of the player.
     */
    public void getMove() {   
        timer = new Timer("Timer");
    
        task = new TimerTask() {
            public void run() {
                game.makeMove(0, 0, true);
                
            }
        };

        long delay = 30000L; // 30 seconds
        timer.schedule(task, delay);
    }

    /**
     * Makes the move in the game and cancels the timer.
     * @param x The X coordinate of the move.
     * @param y The Y coorindate of the move.
     * @pre timer != null, game != null, x >= 0 && x < 15, y >= 0 && y < 10
     * @post ensures that the late move timer is canceled and a move is made in the game
     */
    public void makeMove(int x, int y) {
        timer.cancel();
        game.makeMove(x, y, false);
    }

	
    /**
     * Called by the game when game is over.
     * @param result What the result of the game is. 0: win, 1: loss, 2: tie
     * @pre view != null, 0 =< results >= 2
     * @post ensures that the user is informed about the result of the game
     */
    public void gameOver(int result) {
        view.showEmptyLines(2);
        if (result == 0) {
            view.showMessageLn(TerminalColors.GREEN_BOLD + "> Game over: You won!" + TerminalColors.RESET);
        } else if (result == 1) {
            view.showMessageLn(TerminalColors.RED_BOLD + "> Game over: You lost." + TerminalColors.RESET);
        } else if (result == 2) {
            view.showMessageLn(TerminalColors.RED_BOLD + "> Game over: It's a tie." + TerminalColors.RESET);
        }
        view.showEmptyLines(1);
        view.showMessage("> Enter q to exit game: ");
    }

    /**
     * Prints the player's and enemies boards. The human player's above the computer's
     * @param message The message to be displayed below the boards about what happened in the previous move.
     * @param color The color of the message to be displayed below the boards.
     * @pre message != null, color != null, game != null, board != null, enemyBoard != null
     * @post ensures that both enemy and own board are printed with correct names and scores
     */
    public void printBoards(String message, String color) {
        view.showEmptyLines(3);
        view.printBoard(board.getBoard(), game.getHumanPlayerPoints(), playerName);
        view.showEmptyLines(4);
        view.printEnemyBoard(enemyBoard.getBoard(), game.getComputerPlayerPoints(), "Computer");
        view.showEmptyLines(3);
        view.showMessageLn(color + message + TerminalColors.RESET);
        view.showEmptyLines(1);
        view.showMessage("> Enter coordinates: ");
    }

    /**
     * {@inheritDoc}
     * @pre x >= 0 && x < 15, y >= 0 && y < 10, board != null
    * @post ensures that a move is made on the human players board and correct results
     * about whether ship was hit, sunk and whether all ships are destroyed are returned
     */
    @Override
	public boolean[] enemyMove(int x, int y) {
		return board.singlePlayerMakeMove(x, y);
    }

    /**
     * {@inheritDoc}
     * @pre x >= 0 && x < 15, y >= 0 && y < 10, enemyBoard != null
     * @post ensures that the enemy's board is updated so that in future the human
     * player wouldn't make a move on the same field again
     */
	@Override
	public void update(int x, int y, boolean isHit) {
		enemyBoard.makeMove(x, y, isHit);
		printBoards("You hit enemies ship. Go again!", TerminalColors.GREEN_BOLD);
	}
}
