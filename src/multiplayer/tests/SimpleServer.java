 package multiplayer.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import protocol.ProtocolMessages;

public class SimpleServer {
    public static final int PORT = 8888;
    PrintWriter out;
    BufferedReader in;
    ServerSocket serverSocket;
    public static void main(String[] args) {
        new SimpleServer();
    }

    public SimpleServer() {
        setup();
        listen();
    }

    public void setup() {
        try {
			serverSocket = new ServerSocket(SimpleServer.PORT);
		} catch (IOException e) {
            e.printStackTrace();
		}
    }

    public void listen() {
        try {
            Socket socket = serverSocket.accept();
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            /* Handshake */
            String handshake = in.readLine();
            try {
                if (handshake.split(";")[0].equals(ProtocolMessages.HANDSHAKE) && !handshake.split(";")[1].isEmpty()) {
                    out.println(ProtocolMessages.HANDSHAKE);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
