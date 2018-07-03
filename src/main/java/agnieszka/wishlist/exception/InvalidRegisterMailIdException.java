package agnieszka.wishlist.exception;

public class InvalidRegisterMailIdException extends Exception {

	public InvalidRegisterMailIdException() {
		super();
	}

	public InvalidRegisterMailIdException(String message, Exception cause) {
		super(message, cause);
	}

	public InvalidRegisterMailIdException(String message) {
		super(message);
	}
}
