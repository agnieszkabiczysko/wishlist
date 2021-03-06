package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.converter.EmailFromString;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@Spy
	private List<User> users = new ArrayList<>();
	
	@Spy
	private User user;
	
	@Mock
	private UserDao dao;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private EmailAddress emailAddress;
	
	@Mock
	private EmailFromString emailFromString;
	
	@Before
	public void setup() {
		user = getUser();
		users = getUsers();
	}
	
	@Test
	public void saveUserinDb() {
		//given
		doNothing().when(dao).save(any(User.class));
		
		//when
		userService.save(any(User.class));
		
		//then
		verify(dao, times(1)).save(any(User.class));
	}
	
	@Test
	public void updateUser() {
		//given
		doNothing().when(dao).merge(any(User.class));
		
		//when
		userService.update(any(User.class));
		
		//then
		verify(dao, times(1)).merge(any(User.class));
	}
	
	@Test
	public void findUserById() {
		//given
		when(dao.findUserById(anyInt())).thenReturn(user);
		
		//when
		User userFound = userService.findUserById(anyInt());
		
		//then
		verify(dao, times(1)).findUserById(anyInt());
		assertThat(userFound).isEqualTo(user);
	}
	
	@Test
	public void findUserByUserId() {
		//given
		when(dao.findUserByUserId(anyString())).thenReturn(user);
		
		//when
		User userFound = userService.findUserByUserId(anyString());
		
		//then
		verify(dao, times(1)).findUserByUserId(anyString());
		assertThat(userFound).isEqualTo(user);
	}
	
	@Test
	public void findUserByEmail() {
		//given
		when(dao.findUserByEmail(any(EmailAddress.class))).thenReturn(user);
		
		//when
		User userFound = userService.findUserByEmail(any(EmailAddress.class));
		
		//then
		verify(dao, times(1)).findUserByEmail(any(EmailAddress.class));
		assertThat(userFound).isEqualTo(user);
	}

	
	@Test
	public void findUserByEmailString() {
		//given
		when(dao.findUserByEmail(any(EmailAddress.class))).thenReturn(user);
		
		//when
		User userFound = userService.findUserByEmail(anyString());
		
		//then
		verify(dao, times(1)).findUserByEmail(any(EmailAddress.class));
		assertThat(userFound).isEqualTo(user);
	}
	
	@Test
	public void setPassword() {
		//given
		doNothing().when(dao).merge(any(User.class));
		
		//when
		userService.setPasswordAndActivateUser(user, anyString());
		
		//then
		verify(dao, times(1)).merge(any(User.class));
	}
	
	@Test
	public void userIdNotExists() {
		//given
		when(dao.getAllUsers()).thenReturn(users);
		
		//when
		Boolean idIsUnique = userService.userIdExists(user.getUserId());
				
		//then
		assertTrue(idIsUnique);
	}
	
	@Test
	public void userIdExists() {
		//given
		when(dao.getAllUsers()).thenReturn(users);
		
		//when
		Boolean idIsUnique = userService.userIdExists("uniqueId");
		
		//then
		assertFalse(idIsUnique);
	}
	
	@Test
	public void userEmailExists() {
		//given
		when(dao.getAllUsers()).thenReturn(users);
		
		//when
		Boolean emaiIslUnique = userService.userEmailExists(users.get(0).getEmail());
		
		//then
		assertTrue(emaiIslUnique);
	}
	
	@Test
	public void userEmailNotExists() {
		//given
		when(dao.getAllUsers()).thenReturn(users);
		users.get(0).setEmail(emailAddress);
		
		//when
		Boolean emailIsUnique = userService.userEmailExists(new EmailAddress());
		
		//then
		assertFalse(emailIsUnique);
	}
	
	@Test
	public void findAllUser() {
		//given
		when(dao.getAllUsers()).thenReturn(users);
		
		//when
		List<User> usersFound = userService.getAllUsers();
		
		//then
		verify(dao, times(1)).getAllUsers();
		assertThat(usersFound).isEqualTo(users);
	}
	
	@Test
	public void getCurrentUser() {
		//given
		when(dao.findUserByUserId(anyString())).thenReturn(user);
		
		//when
		User userFound = userService.findUserByUserId(anyString());
		
		//then
		verify(dao, times(1)).findUserByUserId(anyString());
		assertThat(userFound).isEqualTo(user);
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");
		user.setEmail(new EmailAddress("user@test"));
		return user;
	}

	private User getSecondUser() {
		User user = new User();
		user.setUserId("U2");
		return user;
	}
	
	private List<User> getUsers() {
		users.add(getUser());
		users.add(getSecondUser());
		return users;
	}
}
