package gameboards.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameboards.ships.Battleship;
import gameboards.ships.Carrier;
import gameboards.ships.Destroyer;
import gameboards.ships.Patrol;
import gameboards.ships.Ship;
import gameboards.ships.SuperPatrol;


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
}
