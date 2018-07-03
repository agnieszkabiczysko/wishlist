package agnieszka.wishlist.model;

public enum WishlistState {

	PUBLIC("public"),
	PRIVATE("private"),
	SHARED("shared");
	
	private String state;

	private WishlistState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return this.state;
	}
	
}
