package client;

// External imports
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

// Internal imports
import exceptions.ExitProgram;
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import gameboard.GameBoard;
import protocol.ClientProtocol;
import protocol.ProtocolMessages;
import tui.GameClientTUI;

public class GameClient implements ClientProtocol {
	// Socket for communication with the server
	private Socket socket;
	
	// Reading and writing buffers for communication with the server
    private ObjectInputStream in;
	private ObjectOutputStream out;
	
	// The TUI for getting user input
	private GameClientTUI view;

	// The user entered player name
	private String playerName;

	// Game board
	private GameBoard board;

    public GameClient() {
		this.view = new GameClientTUI(this);
		board = new GameBoard(false);
    }
	
	public static void main(String[] args) throws ServerUnavailableException, ProtocolException {
		System.out.println("Welcome to the battleship game!");
		GameClient client = new GameClient();
		client.setup();
    }
	
    /**
	 * Sends a message to the connected server, followed by a new line. 
	 * The stream is then flushed.
	 * 
	 * @param message the message to write to the OutputStream.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public void sendMessage(String message) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.writeUTF(message);
                out.flush();
            } catch (IOException e) {
				view.showMessage(e.getMessage());
                throw new ServerUnavailableException("Could not write to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not wrie to server.");
        }
	}
	
	public void sendBoard() throws ServerUnavailableException {
		if (out != null) {
 
			try {
				ObjectOutputStream boardOut = new ObjectOutputStream(socket.getOutputStream());
				boardOut.writeObject(this.board.getBoard());
				boardOut.flush();
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not write to server.");
			}
		} else {
			throw new ServerUnavailableException("Could not write to server");
		}
	}
	
	public void setup() throws ServerUnavailableException, ProtocolException {
		try {
			playerName = view.getString("Enter your player name, try to make it unique: ");
			int port = view.getInt("Enter server port: ");
			createConnection(port);
			handleHello();
			start();
		} catch (ExitProgram e) {
			closeConnection();
		} 
	}


	/**
	 * Continuously listens to server input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 * @throws ServerUnavailableException
	 * @throws ProtocolException
	 */
	public void start() throws ServerUnavailableException, ProtocolException {
		
        String input;
		try {
			input = in.readUTF();
			while (input != null) {
                handleCommand(input);
				input = in.readUTF();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void handleCommand(String input) throws ServerUnavailableException, ProtocolException {
		if (input.equals(ProtocolMessages.HANDSHAKE)) { // Handshake

			view.showMessage("Welcome to the battleship server!");
		
		} else if(input.split(";")[0].equals(ProtocolMessages.ENEMYNAME)) { // Enemy name
		
			view.showMessage("Enemy: " + input.split(";")[1]);
			board = new GameBoard(false);
			sendMessage(ProtocolMessages.CLIENTBOARD);
			sendBoard();
			view.printBoard(board.getBoard());
		
		} else if (input.equals(ProtocolMessages.NAME_EXISTS)) {
			view.showMessage("The name " + playerName + " is the same as your opponents. Please choose a different name.");
			playerName = view.getString("Enter your player name: ");
			handleHello();
		}
		
		// if (!input.isEmpty()) {
		// 	view.showMessage(input);
		// }
    }


	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.HANDSHAKE+ProtocolMessages.DELIMITER+playerName);
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
	
	
	/**
	 * Creates a connection to the server. Requests the IP and port to 
	 * connect to at the view (TUI).
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
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
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
}