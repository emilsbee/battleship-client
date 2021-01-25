package players;

// External imports
import java.util.Random;

// Internal imports
import game.Game;
import gameboard.EnemyGameBoard;
import gameboard.GameBoard;

public class ComputerPlayer implements Player {
    // Game instance
    private Game game;
    
    // The computer player's board
    private GameBoard board;

    // The enemies board to keep track of computer player's moves
    private EnemyGameBoard enemyBoard;

    // Re-usable random instance
    Random random;

    /**
     * Initialises the computer player
     * @param game The game instance this player is a part of
     */
    public ComputerPlayer(Game game) {
        this.game = game;
        this.board = new GameBoard(false);
        this.enemyBoard = new EnemyGameBoard();
        random = new Random();
    }

	@Override
	public void getMove() {
        int x = 0;
        int y= 0;
        boolean validMove = false;
        while (!validMove) {
            x = random.nextInt(15);
            y= random.nextInt(10);
            validMove = enemyBoard.isValidMove(x, y);
        }

        game.makeMove(x, y, false);            
    }
    
    @Override
	public boolean[] enemyMove(int x, int y) {
		return board.singlePlayerMakeMove(x, y);
	}

	@Override
	public void update(int x, int y, boolean isHit) {
        enemyBoard.makeMove(x, y, isHit);

		
    }
    
    
}
