package agnieszka.wishlist.model;

public enum WishState {

	ACTIVE("Active"),
	PURCHASED("Purchased");
	
	private String state;

	private WishState(String state) {
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
