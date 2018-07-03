package agnieszka.wishlist.service;

import java.util.List;

import agnieszka.wishlist.exception.InvalidRegisterMailIdException;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;

public interface RegisterMailService {

	void saveRegistrationMail(RegisterMail RegisterMail);

	RegisterMail createRegisterMail(User user);
	
	RegisterMail findMailByMailingId(String uuid) throws InvalidRegisterMailIdException;
	
	RegisterMail findMailByConfirmationId(String uuid);

	User findUserByMailingId(String uuid) throws InvalidRegisterMailIdException;

	User findUserByConfirmationId(String confirmationId);

	Boolean isRegisterMailActive(RegisterMail registerMail);

	List<RegisterMail> findMailsForUser(User user);
	
}
