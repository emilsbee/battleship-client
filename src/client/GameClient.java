package client;

// External imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// Internal imports
import exceptions.ExitProgram;
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import gameboard.EnemyGameBoard;
import gameboard.GameBoard;
import protocol.ClientProtocol;
import protocol.ProtocolMessages;
import tui.GameClientTUI;
import tui.TerminalColors;

public class GameClient implements ClientProtocol {
	// Socket for communication with the server
	private Socket socket;
	
	// Reading and writing buffers for communication with the server
    // private ObjectInputStream in;
	// private ObjectOutputStream out;
	private BufferedReader in;
    private BufferedWriter out;

	// The TUI for getting user input
	private GameClientTUI view;

	// The user entered player name
	private String playerName;

	// Enemies name
	private String enemyName;

	// The enemy board
	private EnemyGameBoard enemyBoard;

	// Game board
	private GameBoard board;

	// The move class that is put in a thread to get user input for moves
	Move moveObj;

	// The thread in which moveObj is put in
	Thread moveThread;

	// Indicates whether it is currently my move
	boolean myMove;


    public GameClient() {
		this.view = new GameClientTUI(this);
		board = new GameBoard(false);
		enemyBoard = new EnemyGameBoard();
		moveObj = new Move(enemyBoard, this, view);
		moveThread = new Thread(moveObj);
		myMove = false;
    }
	
	public static void main(String[] args) throws ServerUnavailableException, ProtocolException {
		GameClient client = new GameClient();
		client.setup();
    }
	
	/**
	 * Establishes a connection to the server by asking the user for server's port and ip, then 
	 * calling the {@link #createConnection(int)}. Afterwards sends the handshake message to server 
	 * with {@link #handleHello()} and finally uses {@link} {@link #start()} to start the listening 
	 * for server messages. 
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws ProtocolException if there is a messup with a protocol message.
	 */
	public void setup() throws ServerUnavailableException, ProtocolException {
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> Welcome to the battleship game!" + TerminalColors.RESET);
		view.showEmptyLines(1);
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> To make a move in the game enter a-o and 1-10 (example: a,2)" + TerminalColors.RESET);
		try {
			view.showEmptyLines(1);
			playerName = view.getString(TerminalColors.PURPLE_BOLD + "> Enter your player name, try to make it unique: " + TerminalColors.RESET);
			view.showEmptyLines(1);
			int port = view.getInt(TerminalColors.PURPLE_BOLD + "> Enter server port: "+ TerminalColors.RESET);
			createConnection(port);
			handleHello();
			start();
		} catch (ExitProgram e) {
			closeConnection();
		} 
	}
	
    /**
	 * Sends a message to the connected server. 
	 * The stream is then flushed.
	 * @param message the message to write to the ObjectOutputStream.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public void sendMessage(String message) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(message);
				out.newLine();
                out.flush();
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not write to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not wrie to server. ObjectOutputStream is null.");
        }
	}
	


	/**
	 * Continuously listens to server input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 * @throws ServerUnavailableException if IO error occurs.
	 * @throws ProtocolException if there is a messup with a protocol message.
	 */
	public void start() throws ServerUnavailableException, ProtocolException {
		
        String input;
		try {
			input = in.readLine();
			while (input != null) {
                handleCommand(input);
				input = in.readLine();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Deals with the messages received from the server. If the input is a valid protocol message 
	 * it calls the respective methods to deal with the message.
	 * @param input The message from the server that is checked whether it's a valid protocol message. 
	 * @throws ServerUnavailableException if IO error occurs.
	 * @throws ProtocolException if there is a messup with a protocol message.
	 */
	public void handleCommand(String input) throws ServerUnavailableException, ProtocolException {
		if (input.equals(ProtocolMessages.HANDSHAKE)) { // Handshake

			view.showEmptyLines(1);
			view.showMessageLn(TerminalColors.BLUE_BOLD + "> Welcome to the battleship server! Wait until an enemy connects." + TerminalColors.RESET);
			
		} else if(input.split(";")[0].equals(ProtocolMessages.ENEMYNAME)) { // Enemy name
			
			try {
				enemyName = input.split(";")[1];
				enemyName(enemyName);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new ProtocolException("Enemy name was not provided in ENEMYNAME message.");
			}
			
		
		} else if (input.equals(ProtocolMessages.NAME_EXISTS)) { // Name exists

			nameExists();

		} else if (input.split(";")[0].equals(ProtocolMessages.SETUP)) { // Game setup

			try {
				String whoGoesFirstName = input.split(";")[1];
				gameSetup(whoGoesFirstName);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new ProtocolException("Who goes first name wasn't provided in SETUP message.");
			}
		
		} else if (input.split(";")[0].equals(ProtocolMessages.UPDATE)) { // Update

			try {
				String[] splitInput = input.split(";");
				int x = Integer.parseInt(splitInput[1]);
				int y = Integer.parseInt(splitInput[2]);
				boolean isHit = Boolean.parseBoolean(splitInput[3]);
				boolean isSunk = Boolean.parseBoolean(splitInput[4]);
				boolean isLate = Boolean.parseBoolean(splitInput[5]);
				String whoWentName = splitInput[6];
				String whoGoesNextName = splitInput[7];

				update(x, y, isHit, isSunk, isLate, whoWentName, whoGoesNextName);
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				throw new ProtocolException("Update message had problem parsing an integer or one of the values wasn't provided.");
			}
		} else if (input.split(";")[0].equals(ProtocolMessages.GAMEOVER)) { // Game over

			try {
				String winnerName = input.split(";")[1];
				boolean winType = Boolean.parseBoolean(input.split(";")[2]);
				gameOver(winnerName, winType);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new ProtocolException("Game over message didn't include playerName or the win type");
			}

		}
	}


	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.HANDSHAKE+ProtocolMessages.DELIMITER+playerName);
	}
	
	@Override
	public void nameExists() throws ServerUnavailableException, ProtocolException {
		view.showEmptyLines(1);
		view.showMessageLn(TerminalColors.RED_BOLD +  "> The name " + playerName + " is the same as your opponents. Please choose a different name." + TerminalColors.RESET);
		view.showEmptyLines(1);
		playerName = view.getString(TerminalColors.PURPLE_BOLD + "> Enter your player name: " + TerminalColors.RESET);
		handleHello();	
	}

	@Override
	public void enemyName(String enemyName) throws ServerUnavailableException {
		view.showEmptyLines(1);
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> Enemy: " + enemyName + TerminalColors.RESET);
		view.printBoard(board.getBoard(), board.getScore(), playerName);
		view.showEmptyLines(4);
		view.printEnemyBoard(enemyBoard.getBoard(), enemyBoard.getScore(), enemyName);
		clientBoard();
	}

	@Override
	public void clientBoard() throws ServerUnavailableException {
		sendMessage(board.encodeBoard(board.getBoard()));
	}
	
	@Override
	public void gameSetup(String whoGoesFirstName) {
		moveThread.start();
		if (playerName.equals(whoGoesFirstName)) { // If I go first
			view.showEmptyLines(1);
			view.showMessageLn(TerminalColors.GREEN_BOLD +  "> It's your turn!" + TerminalColors.RESET);
			view.showEmptyLines(1);
			myMove = true;
		} else { // If enemy goes first
			view.showEmptyLines(1);		
		}
	}

	@Override
	public void move(int x, int y) {
		if (myMove) {
			try {
				sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + String.valueOf(x) + ProtocolMessages.DELIMITER + String.valueOf(y));
			} catch (ServerUnavailableException e) {
				view.showMessageLn("Could not wrie to server.");
			}  
		} else {
			view.showEmptyLines(1);			
			view.showMessageLn(TerminalColors.RED_BOLD + "> Not your move!" + TerminalColors.RESET);
		}
		
	}
					
	@Override
	public void update(int x, int y, boolean isHit, boolean isSunk, boolean isLate, String whoWentName, String whoGoesNextName) {
		if (isLate) { // If the update indicates that the move was late
				
			if (playerName.equals(whoWentName)) { // If I didn't make the move on time
				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.RED_BOLD +  "> You missed your move." + TerminalColors.RESET);
				view.showEmptyLines(1);
				view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
				myMove = false;
		
			} else { // If opponent didn't make their move on time

				myMove = true;
				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.GREEN_BOLD +  "> It's your turn!" + TerminalColors.RESET);
				view.showEmptyLines(1);
				view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
			}

		} else { // If the update isn't about late move

			if (playerName.equals(whoWentName)) { // If I made the previous move
				
				enemyBoard.makeMove(x, y, isHit);
				board.addScore(isHit, isSunk);
				view.printBoard(board.getBoard(), board.getScore(), playerName);
				view.showEmptyLines(4);
				view.printEnemyBoard(enemyBoard.getBoard(), enemyBoard.getScore(), enemyName);
				if (!isHit) {
					myMove = false;
					view.showEmptyLines(2);
					view.showMessageLn(TerminalColors.RED_BOLD+  "> You missed enemies boat. Wait for your move." + TerminalColors.RESET);
					view.showEmptyLines(1);
					view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
				} else {
					view.showEmptyLines(2);

					if (isSunk) {
						view.showMessageLn(TerminalColors.GREEN_BOLD +  "> You sunk enemies boat. It's your turn again!" + TerminalColors.RESET);
					} else {
						view.showMessageLn(TerminalColors.GREEN_BOLD +  "> You hit enemies boat. It's your turn again!" + TerminalColors.RESET);
					}
					view.showEmptyLines(1);
					view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
				}
 
	
			} else { // If opponent made the previous move
				board.makeMove(x, y);
				enemyBoard.addScore(isHit, isSunk);
				view.printBoard(board.getBoard(), board.getScore(), playerName);
				view.showEmptyLines(4);
				view.printEnemyBoard(enemyBoard.getBoard(), enemyBoard.getScore(), enemyName);
				if (!isHit) {
					myMove = true;
					view.showEmptyLines(2);
					view.showMessageLn(TerminalColors.GREEN_BOLD +  "> Enemy missed your ship. It's your turn!" + TerminalColors.RESET);
					view.showEmptyLines(1);
					view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
				} else {
					myMove = false;
					view.showEmptyLines(2);
					view.showMessageLn(TerminalColors.RED_BOLD +  "> Enemy hit your ship. Wait for your move!" + TerminalColors.RESET);
					view.showEmptyLines(1);
					view.showMessage(TerminalColors.PURPLE_BOLD + "> Enter coordinates: " + TerminalColors.RESET);
				}

			}
		
		}
	}
	
	@Override
	public void gameOver(String winnerName, boolean winType) {
		if (winType) { // If end of game was reached normally

			if (playerName.equals(winnerName)) { // If I win
				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.GREEN_BOLD + "You won! Congratz!" + TerminalColors.RESET);
			} else { // If opponent wins
				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.RED_BOLD + "You lost! Too bad!" + TerminalColors.RESET);
			}

		} else { // If end of game was reached because opponent left
			view.showEmptyLines(2);
			view.showMessageLn(TerminalColors.GREEN_BOLD + "Your opponent left, so you win! Congratz!" + TerminalColors.RESET);
		}
		view.showEmptyLines(1);
		view.showMessage("Type q to exit game: ");
    }
    
    @Override
    public void sendExit() {
		// Stub
	}

	
	public boolean getMyMove() {
		return this.myMove;
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
				view.showEmptyLines(1);
				view.showMessageLn(TerminalColors.BLUE_BOLD + "> Attempting to connect to " + addr + ":" + port + "..." + TerminalColors.RESET);
				socket = new Socket(addr, port);
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				view.showMessageLn("ERROR: could not create a socket on " + host + " and port " + port + ".");
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
		view.showMessageLn("Closing the connection...");
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}