package agnieszka.wishlist.model;

public enum RegisterMailState {

	ACTIVE("Active"),
	INACTIVE("Inactive");

	private String state;

	private RegisterMailState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return state;
	}
}
