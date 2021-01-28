package exceptions;

/**
 * Exception thrown when recieving a message from the server did not follow the defined protocol.
 */
public class ProtocolException extends Exception {

	private static final long serialVersionUID = 4814836969744019085L;

	/**
	 * @pre msg != null
	 * @post ensures that message is passed to super class
	 */
	public ProtocolException(String msg) {
		super(msg);
	}

}