package multiplayer;

// Internal imports
import exceptions.ProtocolException;
import exceptions.ServerUnavailableException;
import gameboards.GameBoard;

/**
 * Interface that must be implemented by a class that communicates with the server.
 */
public interface ClientProtocol {
    
    /**
     * Sends a handshake message by the protocol to the server given a player's name. 
     * @throws ServerUnavailableException If there's an IO problem with the server.
     * @throws ProtocolException If there's a problem with the messages received from the server in terms of protocol.
     */
    public void handleHello(String playerName) throws ServerUnavailableException, ProtocolException;

    /**
     * Prompts the user for a new player name if the one sent initially is taken by the opponent. When
     * user enter the new player name, it calls {@link #handleHello(String)} with the new player name.
     * @throws ServerUnavailableException If there's an IO problem with the server.
     * @throws ProtocolException If there's a problem with the messages received from the server in terms of protocol.
     */
    public void nameExists() throws ServerUnavailableException, ProtocolException;

    /**
    * After server sends the opponents name, this method displays it and prints the user's board
    * and the enemy's board.
    * Then sends the user's board to the server.
    * @param enemyName the name of the opponent 
    * @throws ServerUnavailableException If there's an IO problem with the server.
    */
    public void enemyName(String enemyName) throws ServerUnavailableException ;

    /**
     * Given an instance of a game board, it send the encoded version of the board to the server.
     * @param board The board instance to be encoded and sent to the server.
     * @throws ServerUnavailableException If there's an IO problem with the server.
     */
    public void clientBoard(GameBoard board) throws ServerUnavailableException; 

    /**
    * Receives from the server information about which player makes the first move.
    * Starts the move thread that continuously asks for coordinates of a move.
    * Then if it's the user's move it prints a message informing the user of that
    * and sets the myMove variable to true.
    * @param whoGoesFirstName The name of the player that goes first.
    */
    public void gameSetup(String whoGoesFirstName);

    /**
     * Called by the move thread to send the move to the server if it is the user's move. If it
     * isn't the user's move, it informs the user of that and doesn't send the move to server.
     * @param x The x value of the move.
     * @param y The y value of the move
     * @throws ServerUnavailableException If there's an IO problem with the server.
     */
    public void move(int x, int y) throws ServerUnavailableException; 

    /**
     * The method handles updates received from the server after one of the players have made a move.
     * The three main things the method does based on what the update indicates is informs of the 
     * changes that occured such as whether it's user's move, whether previous move resulted in a hit/sunk ship,
     * updates and prints out the user's board and the user's enemies board. And finally, it switches the myMove
     * variable based on whos move it is supposed to be next.
     * @param x the x value of the previous move
     * @param y the y value of the previous move
     * @param isHit indicates whether previous move was a hit on a ship
     * @param isSunk indicates whether previous move sunk a whole ship
     * @param isLate Indicates whether the previous move was performed late
     * @param whoWentName Name of the player that made the move
     * @param whoGoesNextName Name of the player that's supposed to go next
     */
    public void update(int x, int y, boolean isHit, boolean isSunk, boolean isLate, String whoWentName, String whoGoesNextName);

    /**
     * Method that is called when game ends. This could happen if 5 minutes pass, someone sinks all of opponent's ships
     * or one of the clients disconnects. The method informs user of why the game ended and of its result.
     * @param winnerName The name of the winner or empty string if it's a tie.
     * @param winType True if the game ended normally, false if game ended because one of the players quit.
     */
    public void gameOver(String winnerName, boolean winType);

    /**
     * Sends a message to the server indicating that this client will exit.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void sendExit() throws ServerUnavailableException;
}
