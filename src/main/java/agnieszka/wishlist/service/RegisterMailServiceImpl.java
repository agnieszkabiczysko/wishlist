package agnieszka.wishlist.service;

import static agnieszka.wishlist.model.RegisterMailState.ACTIVE;
import static agnieszka.wishlist.service.helper.UUIDGenerator.generateUUID;
import static java.lang.System.currentTimeMillis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.RegisterMailDao;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;

@Service("registerService")
@Transactional
public class RegisterMailServiceImpl implements RegisterMailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMailServiceImpl.class);

	@Autowired
	private RegisterMailDao dao;
	
	@Autowired
	private UserDao userDao;
	
	private void saveRegistrationMail(RegisterMail registerMail) {
		dao.saveRegistrationMail(registerMail);
	}

	@Override
	public RegisterMail recordRegisterMail(User user) {
		userDao.update(user);
		
		RegisterMail activeMail = new RegisterMail(user, generateUUID(), generateUUID(), currentTimeMillis(), ACTIVE);
		
		saveRegistrationMail(activeMail);
		
		invalidateOtherEmails(user, activeMail);
		
		return activeMail;
	}

	@Override
	public List<RegisterMail> findMailsForUser(User user) {
		return dao.findMailsForUser(user);
	}

	@Override
	public RegisterMail findMailByMailingId(String mailingId) {
		return dao.findMailByMailingId(mailingId);
	}

	@Override
	public RegisterMail findMailByConfirmationId(String confirmationId) {
		return dao.findMailByConfirmationId(confirmationId);
	}

	@Override
	public User findUserByMailingId(String mailingId) {
		return findMailByMailingId(mailingId).getUser();
	}

	@Override
	public User findUserByConfirmationId(String confirmationId) {
		return findMailByConfirmationId(confirmationId).getUser();
	}

	@Override
	public Boolean isRegisterMailActive(RegisterMail registerMail) {
		return registerMail.getState() == ACTIVE;
	}
	
	private void invalidateOtherEmails(User user, RegisterMail activeMail) {
		List<RegisterMail> userMails = findMailsForUser(user);
		userMails
				.stream()
				.filter(RegisterMail::isActive)
				.filter(mail -> !mail.equals(activeMail))
				.forEach(this::invalidate);
	}

	private void invalidate(RegisterMail mail) {
		LOGGER.debug("Mail {} is deactivated for user {}", mail.getMailingId(), mail.getUser().getUserId());
		
		mail.setState(RegisterMailState.INACTIVE);
		
		saveRegistrationMail(mail);
	}

}
