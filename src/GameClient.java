import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient implements ClientProtocol {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
	private GameClientTUI view;
	private String playerName;

    public GameClient() {
        this.view = new GameClientTUI(this);
    }
    public static void main(String[] args) throws ServerUnavailableException, ProtocolException {
		System.out.println("Welcome to the battleship game!");
        (new GameClient()).start();
    }

    public void start() throws ServerUnavailableException, ProtocolException {
        try {
			playerName = view.getString("Enter your player name, try to make it unique: ");
			int port = view.getInt("Enter server port: ");
            createConnection(port);
            handleHello();
            view.start();
        } catch (ExitProgram e) {
            this.closeConnection();
        }
    }

    /**
	 * Creates a connection to the server. Requests the IP and port to 
	 * connect to at the view (TUI).
	 * 
	 * The method continues to ask for an IP and port and attempts to connect 
	 * until a connection is established or until the user indicates to exit 
	 * the program.
	 * 
	 * @throws ExitProgram if a connection is not established and the user indicates to want to exit the program.
	 */
	public void createConnection(int port) throws ExitProgram {
		clearConnection();
		while (socket == null) {
			String host = "127.0.0.1";

			// try to open a Socket to the server
			try {
				InetAddress addr = InetAddress.getByName(host);
				view.showMessage("Attempting to connect to " + addr + ":" + port + "...");
				socket = new Socket(addr, port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + host + " and port " + port + ".");
                throw new ExitProgram("User indicated to exit.");
			}
		}
	}

    /**
	 * Resets the serverSocket and In- and OutputStreams to null.
	 * 
	 * Always make sure to close current connections via closeConnection() 
	 * before calling this method!
	 */
	public void clearConnection() {
		socket = null;
		in = null;
		out = null;
    }
    
    /**
	 * Closes the connection by closing the In- and OutputStreams, as 
	 * well as the socket.
	 */
	public void closeConnection() {
		view.showMessage("Closing the connection...");
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
	 * Reads and returns one line from the server.
	 * 
	 * @return the line sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String readLineFromServer() throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read from server.");
                }
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read from server.");
        }
    }

    /**
	 * Sends a message to the connected server, followed by a new line. 
	 * The stream is then flushed.
	 * 
	 * @param message the message to write to the OutputStream.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public synchronized void sendMessage(String message) throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                view.showMessage(e.getMessage());
                throw new ServerUnavailableException("Could not write to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not wrie to server.");
        }
    }

	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
        sendMessage(ProtocolMessages.HELLO+ProtocolMessages.DELIMITER+playerName);
        String response = readLineFromServer();
        if (response.equals(ProtocolMessages.HELLO)) {
			view.showMessage("Welcome to the battleship game!");
		} else {
			throw new ProtocolException("ERROR: Bad handshake.");
		}
	}

	@Override
	public String update(int x, int y, boolean isHit, boolean isSunk, boolean isTurn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameOver(int result) {
		// TODO Auto-generated method stub
		return null;
    }
    
    @Override
    public void sendExit() {
        // Stub
    }
	@Override
	public String enemyName(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void clientBoard(String[][] board) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String gameSetup() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub
		
	}
}