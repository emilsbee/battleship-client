package multiplayer.tests;

// External imports
import org.junit.jupiter.api.*;

import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import gameboards.GameBoard;
import multiplayer.GameClient;
import protocol.ProtocolMessages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
