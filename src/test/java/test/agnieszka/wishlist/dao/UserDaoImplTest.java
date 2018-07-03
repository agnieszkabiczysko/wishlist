package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserState;

public class UserDaoImplTest extends EntityDaoImplTest {

	@Autowired
	private UserDao dao;
	
	private User user;
	private User user2;
	
	@Before
	public void setup() {
		user = getSampleUser();
		user2 = getSecondUser();
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveUserinDb() {
		//when
		dao.save(user);
		
		//then
		assertThat(dao.getAllUsers().size()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateUserinDb() {
		//given
		user.setFirstname("UżytkownikPoZmianie");
		
		//when
		dao.merge(user);
		
		//then
		assertThat(dao.findUserByUserId(user.getUserId()).getFirstname()).isEqualTo("UżytkownikPoZmianie");
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findUserById() {
		//given
		dao.save(user);
		
		//when
		User userFromDB = dao.findUserById(user.getId());
		
		//then
		assertThat(userFromDB).isEqualTo(user);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findUserByUserId() {
		//given
		dao.save(user);
		
		//when
		User userFromDB = dao.findUserByUserId(user.getUserId());
		
		//then
		assertThat(userFromDB).isEqualTo(user);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllUser() {
		//given
		dao.save(user);
		dao.save(user2);
		
		//when
		List<User> usersFromDb = dao.getAllUsers();
		
		//then
		assertThat(usersFromDb).contains(user, user2);
		assertThat(usersFromDb.size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findUserByEmail() {
		//given
		dao.save(user);
		
		//when
		User userFromDB = dao.findUserByEmail(user.getEmail());
		
		//then
		assertThat(userFromDB).isEqualTo(user);
	}
	
	private User getSampleUser() {
		EmailAddress email = new EmailAddress();
		email.setEmail("testowaniejava@gmail.com");
		User u = new User("testowy", "Użytkownik", "Testowy", email, UserState.ACTIVE);
		return u;
	}

	private User getSecondUser() {
		EmailAddress email = new EmailAddress();
		email.setEmail("testowaniejavaemail@gmail.com");
		User u = new User("testowyDrugi", "UżytkownikDrugi", "TestowyDrugi", email, UserState.ACTIVE);
		return u;
	}
}
