package exceptions;

/**
 * Exception that is thrown in the case that a socket connection to the server couldn't be established.
 */
public class SocketCreationException extends Exception {

	private static final long serialVersionUID = 1L;

	public SocketCreationException(String msg) {
		super(msg);
	}
}
