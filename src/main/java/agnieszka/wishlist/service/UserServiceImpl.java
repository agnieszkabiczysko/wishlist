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
import agnieszka.wishlist.model.UserState;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	
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
	public User getCurrentUser() {
		Authentication authentication = getAuthentication();
		
		if (authentication == null) {
			return null;
		}
		
		org.springframework.security.core.userdetails.User userPrincipal = getUserPrincipal(authentication);
		
	    String userId = userPrincipal.getUsername();
	    
	    return findUserByUserId(userId);
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private org.springframework.security.core.userdetails.User getUserPrincipal(Authentication authentication) {
		return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
	}

}
