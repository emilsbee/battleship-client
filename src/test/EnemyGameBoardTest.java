package test;

import gameboard.EnemyGameBoard;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

public class EnemyGameBoardTest {
    
    // The enemyGameBoard to access all the methods, with randomised placing of the ships
    private EnemyGameBoard enemyGameBoard;

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
        for(int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++){
                assertTrue(newBoard[i][j].equals("WATER"));
                counter++;
            }
        }
        assertTrue(counter == 150); 
    }

    /**
     * 
     */
    @Test
    public void addScoreTest() {
        assertTrue(enemyGameBoard.getScore() == 0); //check if initial value of points = 0

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

        //enemyGameBoard.addScore(false, true); //Did not hit a ship, but did sunk the ship. This should not add any points to the score
        //assertTrue(enemyGameBoard.getScore() == 3);
    }
}
