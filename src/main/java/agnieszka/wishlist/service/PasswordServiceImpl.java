package agnieszka.wishlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.model.Password;

@Service("passwordService")
@Transactional
public class PasswordServiceImpl implements PasswordService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public String createPasswordHash(Password userPassword) {
		return passwordEncoder.encode(userPassword.getPassword());
	}

}
