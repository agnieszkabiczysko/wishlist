package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.UserPreferencesDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;
import agnieszka.wishlist.model.UserState;

public class UserPreferencesDaoImplTest extends EntityDaoImplTest {

	@Autowired
	private UserPreferencesDao dao;
	
	private UserPreferences userPreferences;
	private User user;
	
	@Before
	public void setup() {
		user = getSampleUser();
		userPreferences = getSampleUserPreferences();
		userPreferences.setUser(user);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void saveUserPreferences() {
		//when
		dao.saveUserPreferences(userPreferences);
		
		//then
		assertThat(dao.findUserPreferencesForUser(user)).isEqualTo(userPreferences);
	}
	
	
	private UserPreferences getSampleUserPreferences() {
		return new UserPreferences();
	}

	private User getSampleUser() {
		EmailAddress email = new EmailAddress();
		email.setEmail("testowaniejava@gmail.com");
		User u = new User("testowy", "Użytkownik", "Testowy", email, UserState.ACTIVE);
		return u;
	}
}
