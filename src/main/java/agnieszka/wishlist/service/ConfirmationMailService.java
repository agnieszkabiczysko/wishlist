package agnieszka.wishlist.service;

import agnieszka.wishlist.model.ConfirmationMail;
import agnieszka.wishlist.model.RegisterMail;

public interface ConfirmationMailService {
	ConfirmationMail prepareMail(RegisterMail mailingInstance);
}
