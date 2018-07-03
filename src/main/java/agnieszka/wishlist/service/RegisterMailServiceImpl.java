package agnieszka.wishlist.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.RegisterMailDao;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.exception.InvalidRegisterMailIdException;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.helper.GeneratorUUID;

@Service("registerService")
@Transactional
public class RegisterMailServiceImpl implements RegisterMailService {

	@Autowired
	private RegisterMailDao dao;
	
	@Autowired
	private UserDao userDao;
	
	private final Logger logger = LoggerFactory.getLogger(RegisterMailServiceImpl.class);

	@Override
	public void saveRegistrationMail(RegisterMail registerMail) {
		dao.saveRegistrationMail(registerMail);
	}

	@Override
	public RegisterMail createRegisterMail(User user) {
		userDao.update(user);
		RegisterMail activeMail = new RegisterMail(user, GeneratorUUID.generateUUID(), GeneratorUUID.generateUUID(), System.currentTimeMillis(), RegisterMailState.ACTIVE);
		saveRegistrationMail(activeMail);
		List<RegisterMail> userMails = findMailsForUser(user);
		userMails.stream().filter(RegisterMail::isActive)
						.filter(mail -> !mail.equals(activeMail))
						.forEach(this::invalidate);
		return activeMail;
	}

	@Override
	public List<RegisterMail> findMailsForUser(User user) {
		return dao.findMailsForUser(user);
	}

	@Override
	public RegisterMail findMailByMailingId(String mailingId) 
			throws InvalidRegisterMailIdException {
		RegisterMail mail = dao.findMailByMailingId(mailingId);
		if (mail == null) {
			throw new InvalidRegisterMailIdException();
		}
		return mail;
	}

	@Override
	public RegisterMail findMailByConfirmationId(String confirmationId) {
		return dao.findMailByConfirmationId(confirmationId);
	}

	@Override
	public User findUserByMailingId(String mailingId) 
			throws InvalidRegisterMailIdException {
		User user = findMailByMailingId(mailingId).getUser();
		if (user == null) {
			throw new InvalidRegisterMailIdException();
		}
		return user;
	}

	@Override
	public User findUserByConfirmationId(String confirmationId) {
		return findMailByConfirmationId(confirmationId).getUser();
	}

	@Override
	public Boolean isRegisterMailActive(RegisterMail registerMail) {
		return registerMail.getState().equals(RegisterMailState.ACTIVE);
	}
	
	private void invalidate(RegisterMail mail) {
		logger.debug("Mail {} is deactivated for user {}", mail.getMailingId(), mail.getUser().getUserId());
		mail.setState(RegisterMailState.INACTIVE);
		saveRegistrationMail(mail);
	}

}
