package agnieszka.wishlist.service;

import agnieszka.wishlist.model.Password;

public interface PasswordService {

	String createPasswordHash(Password userPassword);

}
