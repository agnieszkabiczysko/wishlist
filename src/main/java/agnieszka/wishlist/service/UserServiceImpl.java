package agnieszka.wishlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserProfile;
import agnieszka.wishlist.model.UserState;
import agnieszka.wishlist.model.Wishlist;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	
	@Autowired
	private UserPreferencesService preferencesService;
	
	@Override
	public void save(User user) {
		dao.save(user);
	}
	
	@Override
	public void update(User user) {
		dao.merge(user);
	}

	@Override
	public User findUserById(int id) {
		return dao.findUserById(id);
	}
	
	@Override
	public User findUserByUserId(String userId) {
		return dao.findUserByUserId(userId);
	}

	@Override
	public User findUserByEmail(EmailAddress email) {
		return dao.findUserByEmail(email);
	}
	
	@Override
	public User findUserByEmail(String email) {
		return dao.findUserByEmail(new EmailAddress(email));
	}

	@Override
	public void saveRoleUserForUser(User user) {
		user.addUserProfile(new UserProfile());
		update(user);
	}

	@Override
	public void setPasswordAndActivateUser(User user, String userPassword) {
		user.setPassword(userPassword);
		user.setState(UserState.ACTIVE);
		update(user);
	}

	@Override
	public boolean userIdExists(String userId) {
		return getAllUsers().stream().anyMatch(u -> userId.equals(u.getUserId()));
	}

	@Override
	public boolean userEmailExists(EmailAddress email) {
		return getAllUsers().stream().anyMatch(u -> email.equals(u.getEmail()));
	}
	
	@Override
	public List<User> getAllUsers() {
		return dao.getAllUsers();
	}
	
	@Override
	public User getCurrentUser(String name) {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		org.springframework.security.core.userdetails.User currentUser
			= (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
	    String username = currentUser.getUsername();
	    User user = findUserByUserId(username);
		return user;
	}

	@Override
	public void setCurrentWishlist(User user, Wishlist wishlist) {
		preferencesService.updateCurrentWishlist(user, wishlist);
	}

	@Override
	public Wishlist getCurrentWishlist(User user) {
		return preferencesService.findUserPreferencesForUser(user).getCurrentWishlist();
	}

}
