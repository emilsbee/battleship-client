package multiplayer.tests;

// External imports
import org.junit.jupiter.api.*;
import gameboards.GameBoard;
import multiplayer.GameClient;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;

public class GameClientTest {
    public static final int PORT = 8888; 



    @Test
    void testStartSinglePlayer() {
        System.setIn(new ByteArrayInputStream("emils\ns\nq\n".getBytes())); // Enter name, then s for single player and finally q to quit
        GameClient client = new GameClient();
        assertTrue(client.getBoard() instanceof GameBoard); // Assert that a board was created 
    }

    @Test
    void testProtocolMessages() {
        // TODO
    }


}
