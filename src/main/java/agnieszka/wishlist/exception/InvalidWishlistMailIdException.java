package agnieszka.wishlist.exception;

public class InvalidWishlistMailIdException extends Exception {

	public InvalidWishlistMailIdException() {
		super();
	}

	public InvalidWishlistMailIdException(String message, Exception cause) {
		super(message, cause);
	}

	public InvalidWishlistMailIdException(String message) {
		super(message);
	}

}
