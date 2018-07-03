package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.RegisterMailDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserState;

public class RegisterMailDaoImplTest extends EntityDaoImplTest {


	private RegisterMail registerMail;
	private RegisterMail registerMail2;
	private User user;
	
	@Autowired
	private RegisterMailDao dao;
	
	@Before
	public void setup() {
		user = getSampleUser();
		registerMail = getSampleRegisterMail();
		registerMail.setUser(user);
		registerMail2 = getSecondRegisterMail();
		registerMail2.setUser(user);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void saveRegistrationMail() {
		//given
		String mailingId = registerMail.getMailingId();
		
		//when
		dao.saveRegistrationMail(registerMail);
		
		//then
		assertThat(dao.findMailByMailingId(mailingId)).isEqualTo(registerMail);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findMailByMailingId() {
		//given
		dao.saveRegistrationMail(registerMail);
		
		//when
		RegisterMail mailFromDb = dao.findMailByMailingId(registerMail.getMailingId());
		
		//then
		assertThat(mailFromDb).isEqualTo(registerMail);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findMailByConfirmationId() {
		//given
		dao.saveRegistrationMail(registerMail);
		
		//when
		RegisterMail mailFromDb = dao.findMailByConfirmationId(registerMail.getConfirmationId());
		
		//then
		assertThat(mailFromDb).isEqualTo(registerMail);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllRegisterMailForUser() {
		//given
		dao.saveRegistrationMail(registerMail2);
		dao.saveRegistrationMail(registerMail);
		
		//when
		List<RegisterMail> registerMailsFromDb = dao.findMailsForUser(user);
		
		//then
		assertThat(registerMailsFromDb).contains(registerMail, registerMail2);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findActiveMailForUser() {
		//given
		dao.saveRegistrationMail(registerMail2);
		dao.saveRegistrationMail(registerMail);
		
		//when
		RegisterMail activeRegisterMailFromDb = dao.findActiveMailForUser(user);
		
		//then
		assertThat(activeRegisterMailFromDb).isEqualTo(registerMail);
	}
	
	private RegisterMail getSampleRegisterMail() {
		return new RegisterMail(user, "gsadjfljhaws", "fsdgsgffd", 1500747999699L, RegisterMailState.ACTIVE);
	}
	
	private RegisterMail getSecondRegisterMail() {
		return new RegisterMail(user, "kfhsfdhgsdfh", "jhgkdfg", 1500747999613L, RegisterMailState.INACTIVE);
	}
	
	private User getSampleUser() {
		EmailAddress email = new EmailAddress();
		email.setEmail("testowaniejava@gmail.com");
		User u = new User("testowy", "Użytkownik", "Testowy", email, UserState.ACTIVE);
		return u;
	}
}
