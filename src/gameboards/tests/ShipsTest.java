package gameboards.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import constants.GameConstants;
import gameboards.ships.*;
import gameboards.GameBoard;


/**
 * Test program for GameBoard
 * Module 2 programming project
 * @author Baran Gulbey, Emils Bernhards
 * @version $revision: 1.0$
 */
public class ShipsTest {

    // The five type of ships
    private Ship battleship, carrier, destroyer, patrol, superPatrol;

    /**
     * construct and initialize every type of ship before each test
     */
    @BeforeEach
    public void setUp() {
        battleship = new Battleship();
        carrier = new Carrier();
        destroyer = new Destroyer();
        patrol = new Patrol();
        superPatrol = new SuperPatrol();
    }

    /**
     * Test the method getSize which returns the size of this type of ship
     */
    @Test
    public void getSizeTest() {

        //assert that ship.getSize() returns the expected value
        assertEquals(5, carrier.getSize());
        assertEquals(4, battleship.getSize());
        assertEquals(3, destroyer.getSize());
        assertEquals(2, superPatrol.getSize());
        assertEquals(1, patrol.getSize());
    }

    /**
     * Test the method getAmount which returns the amount of ships this type of ship has
     */
    @Test
    public void getAmountTest() {

        //assert that ship.getAmount() returns the expected value
        assertEquals(2, carrier.getAmount());
        assertEquals(3, battleship.getAmount());
        assertEquals(5, destroyer.getAmount());
        assertEquals(8, superPatrol.getAmount());
        assertEquals(10, patrol.getAmount());
    }

    @Test
    public void placeOnBoardTest() {
        String[][] newBoard = new String[15][10];
        GameBoard gameboard = new GameBoard(false);
        
        newBoard = gameboard.initialiseEmptyBoard(newBoard);

        //placing a patrol ship and asserting the fields before and after placing
        assertTrue(newBoard[1][1].equals(GameConstants.FIELD_TYPE_WATER));
        patrol.placeOnBoard(newBoard, 1, 1);
        assertTrue(newBoard[1][1].equals(GameConstants.FIELD_TYPE_PATROL));

        //placing a super patrol ship and asserting the fields before and after placing
        assertTrue(newBoard[5][1].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[6][1].equals(GameConstants.FIELD_TYPE_WATER));
        superPatrol.placeOnBoard(newBoard, 5, 1);
        assertTrue(newBoard[5][1].equals(GameConstants.FIELD_TYPE_SUPER_PATROL_FRONT));
        assertTrue(newBoard[6][1].equals(GameConstants.FIELD_TYPE_SUPER_PATROL_BACK));

        //placing a destroyer ship and asserting the fields before and after placing
        assertTrue(newBoard[6][2].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[7][2].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[8][2].equals(GameConstants.FIELD_TYPE_WATER));
        destroyer.placeOnBoard(newBoard, 6, 2);
        assertTrue(newBoard[6][2].equals(GameConstants.FIELD_TYPE_DESTROYER_FRONT));
        assertTrue(newBoard[7][2].equals(GameConstants.FIELD_TYPE_DESTROYER_MID));
        assertTrue(newBoard[8][2].equals(GameConstants.FIELD_TYPE_DESTROYER_BACK));
        
        //placing a battleship ship and asserting the fields before and after placing
        assertTrue(newBoard[7][7].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[8][7].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[9][7].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[10][7].equals(GameConstants.FIELD_TYPE_WATER));
        battleship.placeOnBoard(newBoard, 7, 7);
        assertTrue(newBoard[7][7].equals(GameConstants.FIELD_TYPE_BATTLESHIP_FRONT));
        assertTrue(newBoard[8][7].equals(GameConstants.FIELD_TYPE_BATTLESHIP_FRONT_MID));
        assertTrue(newBoard[9][7].equals(GameConstants.FIELD_TYPE_BATTLESHIP_BACK_MID));
        assertTrue(newBoard[10][7].equals(GameConstants.FIELD_TYPE_BATTLESHIP_BACK));

        //placing a carrier ship and asserting the fields before and after placing
        assertTrue(newBoard[8][9].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[9][9].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[10][9].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[11][9].equals(GameConstants.FIELD_TYPE_WATER));
        assertTrue(newBoard[12][9].equals(GameConstants.FIELD_TYPE_WATER));
        carrier.placeOnBoard(newBoard, 8, 9);
        assertTrue(newBoard[8][9].equals(GameConstants.FIELD_TYPE_CARRIER_FRONT));
        assertTrue(newBoard[9][9].equals(GameConstants.FIELD_TYPE_CARRIER_FRONT_MID));
        assertTrue(newBoard[10][9].equals(GameConstants.FIELD_TYPE_CARRIER_MID));
        assertTrue(newBoard[11][9].equals(GameConstants.FIELD_TYPE_CARRIER_BACK_MID));
        assertTrue(newBoard[12][9].equals(GameConstants.FIELD_TYPE_CARRIER_BACK));
    }
}
