package agnieszka.wishlist.service;

import java.util.List;

import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;

public interface RegisterMailService {

	RegisterMail recordRegisterMail(User user);
	
	RegisterMail findMailByMailingId(String uuid);
	
	RegisterMail findMailByConfirmationId(String uuid);

	User findUserByMailingId(String uuid);

	User findUserByConfirmationId(String confirmationId);

	Boolean isRegisterMailActive(RegisterMail registerMail);

	List<RegisterMail> findMailsForUser(User user);
	
}
