package singleplayer.players;

// External imports
import java.util.Random;

import gameboards.EnemyGameBoard;
import gameboards.GameBoard;
import singleplayer.game.Game;

/**
 * This class represents the computer player in a singleplayer game. It mainly serves 
 * the purpose of making moves in the game. Although it also keeps track of its game board and 
 * the opponents game board to make better moves. A future potential improvement is created smarter
 * move making as right now it randomly picks a field that it hasn't already fired upon. Also, even though
 * each player has 30 seconds to move it is assumed that the computer will be able to find the empty spot in much
 * less than a 30 seconds, hence no timer was imlemented here. 
 * @inv game != null, board != null, enemyBoard != null, random != null
 */
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
     * @pre game != null
     * @post ensures that game, board, enemyBoad and random are initialised
     */
    public ComputerPlayer(Game game) {
        this.game = game;
        this.board = new GameBoard(false);
        this.enemyBoard = new EnemyGameBoard();
        random = new Random();
    }

    /**
     * {@inheritDoc}
     * @pre random != null, enemyBoard != null, game != null, that enemyBoard still has fields available to make a move on
     * @post ensures that a move is made on a field previously not hit before
     */
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
    
    /**
     * {@inheritDoc}
     * @pre x >= 0 && x < 15, y >= 0 && y < 10, board != null
     * @post ensures that a move is made on the computer players board and correct results
     * about whether ship was hit, sunk and whether all ships are destroyed are returned
     */
    @Override
	public boolean[] enemyMove(int x, int y) {
		return board.singlePlayerMakeMove(x, y);
	}

    /**
     * {@inheritDoc}
     * @pre x >= 0 && x < 15, y >= 0 && y < 10, enemyBoard != null
     * @post ensures that the enemy's board is updated so that in future the computer
     * player wouldn't make a move on the same field again
     */
	@Override
	public void update(int x, int y, boolean isHit) {
        enemyBoard.makeMove(x, y, isHit);

		
    }
    
    
}
