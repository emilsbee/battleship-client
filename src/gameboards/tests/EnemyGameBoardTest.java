package gameboards.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import constants.GameConstants;
import gameboards.EnemyGameBoard;

public class EnemyGameBoardTest {
    
    // The enemyGameBoard to access all the methods, with randomised placing of the ships
    private EnemyGameBoard enemyGameBoard;

    // The board of the enemy
    private String[][] board;

    /**
     * Before each test construct new GameBoard and initialise it, and get the board from the gameBoard
     */
    @BeforeEach
    public void setUp() {
        enemyGameBoard = new EnemyGameBoard();
    }   



    /**
     * Test the method initialiseEmptyBoard, which makes an double String array and fills it with 'WATER' Strings
     */
    @Test
    public void initialiseEmptyBoardTest() {
        String[][] newBoard = new String[15][10];
        newBoard = enemyGameBoard.initialiseEmptyBoard(newBoard);
        int counter = 0;
        for(int i = 0; i < GameConstants.BOARD_SIZE_X; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE_Y; j++){
                assertTrue(newBoard[i][j].equals("WATER"));
                counter++;
            }
        }
        assertTrue(counter == 150); 
    }

    /**
     * Test the method addScore(), which adds a points if a ship has been hit and adds an extra point if the ship has been sunk
     */
    @Test
    public void addScoreTest() {
        assertTrue(enemyGameBoard.getScore() == 0); //Check if initial value of points = 0

        enemyGameBoard.addScore(false, false); //Did not hit a ship
        assertTrue(enemyGameBoard.getScore() == 0);

        enemyGameBoard.addScore(true, false); //Hit a ship, but the ship did not sink
        assertTrue(enemyGameBoard.getScore() == 1); 

        enemyGameBoard.addScore(false, false); //Did not hit a ship, so points should stay 1
        assertTrue(enemyGameBoard.getScore() == 1); 

        enemyGameBoard.addScore(true, true); //Hit a ship, and ship has been sunk
        assertTrue(enemyGameBoard.getScore() == 3);

        enemyGameBoard.addScore(false, false); //Did not hit a ship, so points should stay 3 after a ship has been sunk
        assertTrue(enemyGameBoard.getScore() == 3);

        enemyGameBoard.addScore(false, true); //Did not hit a ship, but did sunk the ship. This should not add any points to the score
        assertTrue(enemyGameBoard.getScore() == 3);
    }

    /**
     * Test the method makeMove(), which makes a move on the enemy-gameboard
     */
    @Test
    public void makeMoveTest() {

        //get the board of the enemy with the method getBoard() from enemyGameBoard
        board = enemyGameBoard.getBoard();

        //try two times to hit a field on the board that is water, and after that assert it is done succesfully
        enemyGameBoard.makeMove(2, 3, false);
        assertTrue(board[2][3].equals(EnemyGameBoard.WATER_HIT));
        enemyGameBoard.makeMove(9, 9, false);
        assertTrue(board[9][9].equals(EnemyGameBoard.WATER_HIT));

        //try two times to hit a field on the board that is not water, and after that assert it is done succesfully
        enemyGameBoard.makeMove(5, 4, true);
        assertTrue(board[5][4].equals(EnemyGameBoard.SHIP_HIT));
        enemyGameBoard.makeMove(8, 1, true);
        assertTrue(board[8][1].equals(EnemyGameBoard.SHIP_HIT));
    }

    /**
     * Test the method isValidMove(), which makes a move on the enemy-gameboard
     */
    @Test
    public void isValidMoveTest() {

        //get the board of the enemy with the method getBoard() from enemyGameBoard
        board = enemyGameBoard.getBoard();

        //Try two times for a existing point on the board
        assertTrue(enemyGameBoard.isValidMove(1,1));
        assertTrue(enemyGameBoard.isValidMove(2,4));

        //Try two times for a non-existing point on the board
        assertFalse(enemyGameBoard.isValidMove(15,5)); // x-value outside the board range
        assertFalse(enemyGameBoard.isValidMove(5,11)); // y-value outside the board range

        //hit a water field
        enemyGameBoard.makeMove(2, 3, false);
        assertTrue(board[2][3].equals(EnemyGameBoard.WATER_HIT));

        //try isValidMove() on the already hit water field
        assertFalse(enemyGameBoard.isValidMove(2,3));

        //hit a ship field
        enemyGameBoard.makeMove(5, 4, true);
        assertTrue(board[5][4].equals(EnemyGameBoard.SHIP_HIT));

        //try isValidMove() on the already hit water field
        assertFalse(enemyGameBoard.isValidMove(5,4));
    }
}
