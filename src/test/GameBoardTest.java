package test;

import gameboard.GameBoard;
import ships.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


/**
 * Test program for GameBoard
 * Module 2 programming project
 * @author Baran Gulbey, Emils Bernhards
 * @version $revision: 1.0$
 */
public class GameBoardTest {

    // The board
    private String[][] board;

    // The gameboard to access all the methods, with randomised placing of the ships
    private GameBoard gameboard;

    /**
     * Before each test construct new GameBoard and initialise it, and get the board from the gameBoard
     */
    @BeforeEach
    public void setUp() {
        gameboard = new GameBoard(false);
        board = gameboard.getBoard();
    }

    /**
     * Test the method initialiseEmptyBoard, which makes an double String array and fills it with 'WATER' Strings
     */
    @Test
    public void initialiseEmptyBoardTest() {
        String[][] newBoard = new String[15][10];
        newBoard = gameboard.initialiseEmptyBoard(newBoard);
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
     * Test the method generateBoard
     */
    @Test
    public void generateBoardTest() {
        int waterCounter = 0;
        int shipCounter = 0;
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++){
                if(board[i][j].equals("WATER")) {
                    waterCounter++;
                }
                else {
                    shipCounter++;
                }
            }
        }
        assertTrue(waterCounter == 87);
        assertTrue(shipCounter == 63);
    }

    /**
     * Test the method findPlaceOnBoard
     */
    @Test
    public void findPlaceOnBoardTest() {

        //make a new board to test the method
        String[][] newBoard = new String[15][10];
        newBoard = gameboard.initialiseEmptyBoard(newBoard);

        //Place all the ships on the board
        gameboard.findPlaceOnBoard(new Carrier(), newBoard);
        gameboard.findPlaceOnBoard(new Battleship(), newBoard);
        gameboard.findPlaceOnBoard(new Destroyer(), newBoard);
        gameboard.findPlaceOnBoard(new SuperPatrol(), newBoard);
        gameboard.findPlaceOnBoard(new Patrol(), newBoard);

        //counters for the different fields that can be on a new board
        int waterCounter = 0;
        int shipCounter = 0;

        //iterate over the whole board
        for(int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++){
                if(newBoard[i][j].equals("WATER")) {
                    waterCounter++;
                }
                else {
                    shipCounter++;
                }
            }
        }

        //assert that the amount of water and ship tiles are correct
        assertTrue(waterCounter + shipCounter == 150);
        assertTrue(waterCounter == 87);
        assertTrue(shipCounter == 63);
    }

    /**
     * Test the method doesFit
     */
    @Test
    public void doesFitTest() {

        //make a new board to test the method
        String[][] newBoard = new String[15][10];
        newBoard = gameboard.initialiseEmptyBoard(newBoard);

        //asserting 
        assertTrue(gameboard.doesFit(1, 1, 1, newBoard)); //check if a patrol fits on a valid coordinate
        assertTrue(gameboard.doesFit(5, 5, 5, newBoard)); //check if a carrier fits on a valid coordinate
        assertFalse(gameboard.doesFit(17, 5, 2, newBoard)); //check if a superPatrol fits on a invalid coordinate
        assertFalse(gameboard.doesFit(12, 8, 4, newBoard)); //check if a battleship fits on a invalid coordinate, out of the board x>15
        
        Patrol ship = new Patrol(); //construct Patrol ship
        ship.placeOnBoard(newBoard, 1, 1); //place Patrol ship on the board on (1,1)
        assertFalse(gameboard.doesFit(1, 1, 3, newBoard)); //check if a destroyer fits on a invalid coordinate, where there is a boat already
    }
    /**
     * Test if the amount of ships are correct for each type of ship
     */
    @Test
    public void shipsOnBoardTest() {
        List<Ship> ships = gameboard.getShips();
        int[] shipsCounter = new int[5]; // counter array for: i-0 = Patrol, 1 = SuperPatrol, 2 = Destroyer, 3 = Battleship, 4 = Carrier
        int size; 
        for(Ship s : ships) { //iterate over the s ship in ships, and decide which ship it is to higher the counter
            size = s.getSize();
            switch(size) {
                case 1:
                shipsCounter[0]++;
                break;
                case 2:
                shipsCounter[1]++;
                break;
                case 3:
                shipsCounter[2]++;
                break;
                case 4:
                shipsCounter[3]++;
                break;
                case 5:
                shipsCounter[4]++;
                break;
            }
        }

        // assert that all counters are the correct value for the amount of ships
        assertTrue(shipsCounter[0] == 10);
        assertTrue(shipsCounter[1] == 8);
        assertTrue(shipsCounter[2] == 5);
        assertTrue(shipsCounter[3] == 3);
        assertTrue(shipsCounter[4] == 2);
        assertTrue(shipsCounter[0]+shipsCounter[1]+shipsCounter[2]+shipsCounter[3]+shipsCounter[4] == 28); //assert that there are 28 ships
    }
}