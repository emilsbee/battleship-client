package multiplayer;

// External imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// Internal imports
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import exceptions.SocketCreationException;
import gameboards.EnemyGameBoard;
import gameboards.GameBoard;
import protocol.ClientProtocol;
import protocol.ProtocolMessages;
import singleplayer.game.Game;
import tui.GameClientTUI;
import tui.TerminalColors;

/**
 * This class represents the communication with the server. It sends and receives messages with/from the server. It also handles the incoming messages 
 * with the methods that are overriden from the ClientProtocol. The message handler methods use the TUI to show the user what has happened in the game
 * and also communicates with the players game board for game logic related matters. Mostly just creating a board and updating the board.
 */
public class GameClient implements ClientProtocol {
	// Socket for communication with the server
	private Socket socket;
	
	// Reading and writing buffers for communication with the server
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
	private Move moveObj;

	// The thread in which moveObj is put in
	private Thread moveThread;

	// Indicates whether it is currently my move
	private boolean myMove;


	/**
	 * Initialises the TUI and a new game board. Then calls {@link #setup()}.
	 */
    public GameClient() {
		this.view = new GameClientTUI();
		board = new GameBoard(false);
		setup();
    }
	
	public static void main(String[] args) {
		new GameClient();
    }
	
	/**
	 * Prompts the user about their player name and whether they want to play multiplayer or singleplayer.
	 * If player chooses to play multiplayer then an enemy board is created, new move thread is created, and a connection to the server is created 
	 * that is followed by handshake and calling {@link #start()} to listen to server messages. 
	 * If player chooses to play single player then a new instance of a game is created and the game handles all singleplayer matters there.
	 * @throws ServerUnavailableException If IO error occurs when communicating with the server.
	 * @throws ProtocolException If there is a messup with a protocol message.
	 * @throws IOException If a general IO error occurs not related to communcation with server.
	 */                                             
	public void setup() {
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> Welcome to the battleship game!" + TerminalColors.RESET);
		view.showEmptyLines(1);

		playerName = view.getString(TerminalColors.PURPLE_BOLD + "> Enter your player name, try to make it unique: " + TerminalColors.RESET);
		view.showEmptyLines(1);
		
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> To make a move in the game enter a-o and 1-10 (example: a,2)" + TerminalColors.RESET);
		String gameType = view.getGameType();

		if (gameType.equalsIgnoreCase("m")) { // Multiplayer

			enemyBoard = new EnemyGameBoard();
			moveObj = new Move(enemyBoard, this, view);
			moveThread = new Thread(moveObj);
			myMove = false;
			
			try {
				createConnection();
			} catch (SocketCreationException sce) {
				view.showMessageLn(TerminalColors.RED_BOLD+sce.getMessage()+TerminalColors.RESET);
				System.exit(0);
			}

			try {
				handleHello(playerName);
			} catch (ServerUnavailableException sue) {
				view.showMessageLn(TerminalColors.RED_BOLD+sue.getMessage()+TerminalColors.RESET);
				System.exit(0);
			}

			try {
				start();
			} catch (ProtocolException | ServerUnavailableException pe) {
				view.showMessageLn(TerminalColors.RED_BOLD+pe.getMessage()+TerminalColors.RESET);
				System.exit(0);
			} 

		} else { // Single player

			new Game(playerName, view);
		
		}

	}


	/**
	 * Creates a connection to the server. Requests the IP and port to 
	 * connect to at the view (TUI).
	 * The method continues to ask for an IP and port and attempts to connect 
	 * until a connection is established or until the user indicates to exit 
	 * the program.
	 * @throws ServerUnavailableException
	 */
	public void createConnection() throws SocketCreationException {
		clearConnection();
		while (socket == null) {
			view.showEmptyLines(1);
			String host = view.getString(TerminalColors.PURPLE_BOLD + "> Enter the ip address of the server: " + TerminalColors.RESET);
			view.showEmptyLines(1);
			int port = view.getInt(TerminalColors.PURPLE_BOLD + "> Enter server port: "+ TerminalColors.RESET);
			
			// try to open a Socket to the server
			try {
				InetAddress addr = InetAddress.getByName(host);
				view.showEmptyLines(1);
				view.showMessageLn(TerminalColors.BLUE_BOLD + "> Attempting to connect to " + addr + ":" + port + "..." + TerminalColors.RESET);
				socket = new Socket(addr, port);
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException | IllegalArgumentException e) {
				throw new SocketCreationException("Error while creating a connection to the server. " +e.getMessage());
			}
		}
	}
	
	/**
	 * Resets the serverSocket and In- and OutputStreams to null.
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
			System.exit(0);
		} catch (IOException e) {
			view.showMessageLn("Error closing connection. Exiting program.");
			System.exit(0);
		}
	}
	
    /**
	 * Sends a message to the connected server followed by a new line. 
	 * The stream is then flushed.
	 * @param message The message to send to the server.
	 * @throws ServerUnavailableException if IO errors occurs.
	 */
	public void sendMessage(String message) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(message);
				out.newLine();
                out.flush();
            } catch (IOException e) {
				throw new ServerUnavailableException("Could not write to server. Exiting program.");
            }
        } else {
			throw new ServerUnavailableException("Could not write to server. Exiting program.");
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
			throw new ServerUnavailableException("Could not read from server. Exiting program.");
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

	/**
	 * Getter for myMove
	 * @return Whether it is my move or not.
	 */
	public boolean getMyMove() {
		return this.myMove;
	}

	/**
	 * Getter for the client board
	 * @return The client's board
	 */
	public GameBoard getBoard() {
		return board;
	}


	@Override
	public void handleHello(String playerName) throws ServerUnavailableException {

		sendMessage(ProtocolMessages.HANDSHAKE+ProtocolMessages.DELIMITER+playerName);
	}
	
	@Override
	public void nameExists() throws ServerUnavailableException {
		view.showEmptyLines(1);
		view.showMessageLn(TerminalColors.RED_BOLD +  "> The name " + playerName + " is the same as your opponents. Please choose a different name." + TerminalColors.RESET);
		view.showEmptyLines(1);
		playerName = view.getString(TerminalColors.PURPLE_BOLD + "> Enter your player name: " + TerminalColors.RESET);
		handleHello(playerName);	
	}

	@Override
	public void enemyName(String enemyName) throws ServerUnavailableException {
		view.showEmptyLines(1);
		view.showMessageLn(TerminalColors.BLUE_BOLD + "> Enemy: " + enemyName + TerminalColors.RESET);
		view.printBoard(board.getBoard(), board.getScore(), playerName);
		view.showEmptyLines(4);
		view.printEnemyBoard(enemyBoard.getBoard(), enemyBoard.getScore(), enemyName);
		clientBoard(board);
	}

	@Override
	public void clientBoard(GameBoard board) throws ServerUnavailableException {
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
	public void move(int x, int y) throws ServerUnavailableException {
		if (myMove) {
			sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + String.valueOf(x) + ProtocolMessages.DELIMITER + String.valueOf(y));
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
				view.showMessageLn(TerminalColors.GREEN_BOLD +  "> Enemy missed their turn. It's your turn!" + TerminalColors.RESET);
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
			
			} else if (winnerName.isEmpty()) { // If it's a tie
			
				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.GREEN_BOLD + "It's a tie! That's alright." + TerminalColors.RESET);
			
			} else { // If enemy wins

				view.showEmptyLines(2);
				view.showMessageLn(TerminalColors.RED_BOLD + "You lost! Too bad." + TerminalColors.RESET);

			}

		} else { // If end of game was reached because opponent left

			view.showEmptyLines(2);
			view.showMessageLn(TerminalColors.GREEN_BOLD + "Your opponent left, so you win! Congratz!" + TerminalColors.RESET);
		
		}
		view.showEmptyLines(1);
		view.showMessage("Type q to exit game: ");
    }
    
    @Override
    public void sendExit() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.EXIT);
	}

}