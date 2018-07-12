package agnieszka.wishlist.service;

import java.util.List;

import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;

public interface UserService {

	void save(User user);

	User findUserById(int id);
	
	User findUserByUserId(String ssoId);
	
	User findUserByEmail(EmailAddress email);
	
	User findUserByEmail(String email);

	void setPasswordAndActivateUser(User user, String userPassword);

	void update(User user);

	boolean userIdExists(String ssoId);
	
	boolean userEmailExists(EmailAddress email);
	
	List<User> getAllUsers();

	User getCurrentUser();

}
