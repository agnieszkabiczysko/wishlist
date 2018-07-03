package agnieszka.wishlist.service;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;
import agnieszka.wishlist.model.Wishlist;

public interface UserPreferencesService {
	
	void saveUserPreferences(UserPreferences preferences);
	
	UserPreferences findUserPreferencesForUser(User user);

	void updateCurrentWishlist(User user, Wishlist wishlist);

	boolean userHasCurrentWishlist(User currentUser);
}
