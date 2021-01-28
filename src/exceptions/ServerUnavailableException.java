package exceptions;

/**
 * Exception thrown when the reading or writing to server couldn't be done due to some IO error.
 */
public class ServerUnavailableException extends Exception {

	private static final long serialVersionUID = -1207009873596120108L;

	/**
	 * @pre msg != null
	 * @post ensures that message is passed to super class
	 */
	public ServerUnavailableException(String msg) {
		super(msg);
	}

}