package agnieszka.wishlist.model;

public class ConfirmationMail {
	
	private final String title;
	private final String body;

	public ConfirmationMail(String title, String body) {
		this.title = title;
		this.body = body;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getBody() {
		return body;
	}

}
