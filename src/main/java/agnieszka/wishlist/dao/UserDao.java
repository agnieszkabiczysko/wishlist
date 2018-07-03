package agnieszka.wishlist.dao;

import java.util.List;

import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;

public interface UserDao {

	void save(User user);

	User findUserById(int id);
	
	User findUserByUserId(String ssoId);

	void merge(User user);

	List<User> getAllUsers();

	User findUserByEmail(EmailAddress email);
	
	void update(User user);

}
