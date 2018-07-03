package test.agnieszka.wishlist.ui;

public class SeleniumRunnerData {

	public String defaultUser() {
		return "marysia";
	}

	public String passwordForDefaultUser() {
		return "";
	}
	
	public String passwordForApplicationForDefaultUser() {
		return "";
	}
	
	public String lastnameDefaultUser() {
		return "Lala";
	}
	
	public String firstnameDefaultUser() {
		return "Marysia";
	}

	public String emailForDefaultUser() {
		return "wishlistp@gmail.com";
	}

	public String defaultWishlistToDefaultUser() {
		return "Urodzinowa";
	}

	public String secondUser() {
		return "Ala";
	}
	
	public String lastnameSecondUser() {
		return "LalaAla";
	}
	
	public String firstnameSecondUser() {
		return "Alicja";
	}

	public String passwordForSecondUser() {
		return "";
	}
	
	public String passwordForApplicationForSecondUser() {
		return "";
	}

	public String emailForSecondUser() {
		return "przepisywspringu@gmail.com";
	}

	public String friendForDefaultUser() {
		return secondUser();
	}

	public String passwordForFriendOfDefaultUser() {
		return passwordForApplicationForSecondUser();
	}

	public String testUserName() {
		return "Testowy";
	}
	
	public String lastnameTestUser() {
		return "Testowy";
	}
	
	public String firstnameTestUser() {
		return "Użytkownik";
	}

	public String passwordForTestUser() {
		return "";
	}

	public String emailForTestUser() {
		return "testowaniejava@gmail.com";
	}

	public String defaultWishlistToTestUser() {
		return "Testowa";
	}
	
	public String offerName() {
		return "OfertaTestowa";
	}
	
	public String offerVendor() {
		return "TestowyProducent";
	}
	
	public String offerDescription() {
		return "OfertaTestowa";
	}
	
	public String publicWishlistName() {
		return "Publiczna";
	}
	
	public String sharedcWishlistName() {
		return "Dzielona";
	}
	
	public String privateWishlistName() {
		return "Prywatna";
	}

	public String noDefaultWishlistName() {
		return "InnaWishlista";
	}

	public String nextOfferName() {
		return "KolejnaOferta";
	}
}
