package protocol;

import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;

public interface ClientProtocol {
    /**
     * Handles the following server-client handshake: 
     * 1. Client sends ProtocolMessages.HELLO ProtocolMessages.DELIMITER + playerName to server 
     * 2. Server returns one line containing ProtocolMessages.HELLO
     * This method sends the HELLO and player's name and checks whether the server response is valid
     * (must contain HELLO). 
     * If the response is not valid, this method throws a ProtocolException. 
     * If the response is valid, a welcome message is forwarded to the view.
     * @throws ServerUnavailableException if IO errors occur.
     * @throws ProtocolException          if the server response is invalid.
     */
    public void handleHello() throws ServerUnavailableException, ProtocolException;

    public void nameExists() throws ServerUnavailableException, ProtocolException;

    /**
    * Method to use to get the other players name given. This is received from server.
    * The received message construct: ProtocolMessages.ENEMYNAME + ProtocolMessages.DELIMITER + playerName
    * @param playerName the name of the player requesting the enemies name 
    * @return the enemy name to send to the other player. 
    */
    public void enemyName(String enemyName) throws ServerUnavailableException ;

    /**
     * Generates a gameboard given a randomly generated fields with ships/water. Client sends their generated board to the server.
     * Message construct: ProtocolMessages.CLIENTBOARD + ProtocolMessages.DELIMITER + board[][]
     * @param board A double String array that represents the board. Contains values from the enum fieldState that make water and ships.
     * @return GameBoard to be sent to server  as their board. 
     */
    public void clientBoard() throws ServerUnavailableException; 

    /**
    * Receives from the server information about which player makes the first move.
    * Message construct: ProtocolMessages.SETUP + ProtocolMessages.DELIMITER + playerName
    * @return the name of player that goes first.
    */
    public void gameSetup(String whoGoesFirstName);

    /**
     * A move that the client makes and sends to server.
     * Message construct: ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + xCoordinate + ProtocolMessages.DELIMITER + yCoordinate
     * @param x The x value of the move.
     * @param y The y value of the move
     * @throws ServerUnavailableException
     */
    public void move(int x, int y) throws ServerUnavailableException; 

    /**
     * Method to update both clients after one of them has made a move. The update
     * includes the x,y coordinates of the move, whether the move was a hit, and whether
     * the move sunk a ship and finally whether the client has the next move. This method is received from the server.
     * Message construct: ProtocolMessages.UPDATE + ProtocolMessages.DELIMITER + xCoordinate +
     * ProtocolMessages.DELIMITER + yCoordinate + ProtocolMessages.DELIMITER + isHit + ProtocolMessages.DELIMITER +
     * isSunk + ProtocolMessages.DELIMITER + playerName
     * @param x the x value of the previous move
     * @param y the y value of the previous move
     * @param isHit indicates whether previous move was a hit on a ship
     * @param isSunk indicates whether previous move sunk a whole ship
     * @param isTurn indicates whether the client has the move now
     * @return the update to both clients about the previous move.
     */
    public void update(int x, int y, boolean isHit, boolean isSunk, boolean isLate, String whoWentName, String whoGoesNextName);

    /**
     * Method that is called when game ends. This could happen if 5 minutes pass, someone sinks all of opponent's ships
     * or one of the clients disconnects. This message is received from the server.
     * * The message construct: ProtocolMessages.GAMEOVER + ProtocolMessages.DELIMITER + result 
     * @param result integer from 0 to 2. 0: win, 1: lose, 2:tie.
     * @return the result of the game which is an int from 0 to 2 that represents whether the client won: 0, lost:1 or it is a tie:2.
     */
    public void gameOver(String playerName, boolean winType);

    /**
     * Sends a message to the server indicating that this client will exit:
     * Message construct: ProtocolMessages.EXIT
     * Both the server and the client then close the connection. The client does
     * this using the {@link #closeConnection()} method.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void sendExit() throws ServerUnavailableException;
}
