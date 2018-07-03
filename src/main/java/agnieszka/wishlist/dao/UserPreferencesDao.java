package agnieszka.wishlist.dao;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;

public interface UserPreferencesDao {

	void saveUserPreferences(UserPreferences preferences);
	
	UserPreferences findUserPreferencesForUser(User user);
}
