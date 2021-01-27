package multiplayer.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;


import multiplayer.Move;

public class MoveTest {
    
    @Test
    void testIndexOfLetterInAlphabet() {
        Move move = new Move();

        // Asserts that the letters from the alphabet array return an actual index
        for (String letter : Move.alphabet) {
            assertTrue(move.indexOfLetterInAlphabet(letter) != -1);
        }

        // Asserts that a letter not from the alphabet array returns -1 which means it has no index
        assertTrue(move.indexOfLetterInAlphabet("z") == -1);
    }
}
