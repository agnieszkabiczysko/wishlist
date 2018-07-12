package agnieszka.wishlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.UserPreferencesDao;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;
import agnieszka.wishlist.model.Wishlist;

@Service("userPreferencesService")
@Transactional
public class UserPreferencesServiceImpl implements UserPreferencesService {

	@Autowired
	private UserPreferencesDao dao;
	
	@Override
	public void saveUserPreferences(UserPreferences userPreferences) {
		dao.saveUserPreferences(userPreferences);
	}

	@Override
	public UserPreferences findUserPreferencesForUser(User user) {
		return dao.findUserPreferencesForUser(user);
	}

	@Override
	public void updateCurrentWishlist(User user, Wishlist wishlist) {
		UserPreferences userPreferences = findUserPreferencesForUser(user);
		if (userPreferences == null) {
			userPreferences = new UserPreferences(user, wishlist);
		} else {
			userPreferences.setCurrentWishlist(wishlist);
		}
		saveUserPreferences(userPreferences);
	}

	@Override
	public boolean userHasCurrentWishlist(User user) {
		return findUserPreferencesForUser(user) != null;
	}

}
