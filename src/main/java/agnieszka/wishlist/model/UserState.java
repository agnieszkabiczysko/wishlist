package agnieszka.wishlist.model;

public enum UserState {

	ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted"),
    LOCKED("Locked");
     
    private String state;

	private UserState(final String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
    
	@Override
	public String toString(){
		return state;
	}
	 
	public String getName(){
		return name();
	}
	 
}
