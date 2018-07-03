package agnieszka.wishlist.dao;

import java.util.List;

import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;

public interface RegisterMailDao {

	void saveRegistrationMail(RegisterMail registerMail);

	RegisterMail findMailByMailingId(String uuid);
	
	RegisterMail findMailByConfirmationId(String uuid);

	List<RegisterMail> findMailsForUser(User user);

	RegisterMail findActiveMailForUser(User user);
	
}
