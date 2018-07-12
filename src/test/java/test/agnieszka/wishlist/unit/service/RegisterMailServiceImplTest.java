package test.agnieszka.wishlist.unit.service;

import static agnieszka.wishlist.model.RegisterMailState.ACTIVE;
import static agnieszka.wishlist.model.RegisterMailState.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import org.slf4j.Logger;

import agnieszka.wishlist.dao.RegisterMailDao;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.RegisterMailServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMailServiceImplTest {

	@Mock
	private RegisterMailDao dao;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private Logger logger;
	
	@Mock
	private User user;
	
	@InjectMocks
	private RegisterMailServiceImpl mailService;
	
	@Spy
	private RegisterMail registerMail;
	
	@Spy
	private RegisterMail inactiveRegisterMail;

	private List<RegisterMail> registerMailList = new ArrayList<>();
	
	@Before
	public void setup() {
		when(registerMail.isActive()).thenReturn(true);
		when(inactiveRegisterMail.isActive()).thenReturn(false);
		
		registerMailList.add(registerMail);
		registerMailList.add(inactiveRegisterMail);
	}
	
	@Test
	public void createRegisterMail() {
		//given
		User user = mock(User.class);
		when(user.getUserId()).thenReturn("testUser");
		
		doNothing().when(userDao).update(user);
		doNothing().when(dao).saveRegistrationMail(any(RegisterMail.class));
		when(dao.findMailsForUser(any(User.class))).thenReturn(registerMailList);
		
		when(registerMail.getUser()).thenReturn(user);
		
		//when
		RegisterMail newRegisterMail = mailService.recordRegisterMail(any(User.class));
		
		//then
		verify(dao, times(1)).saveRegistrationMail(newRegisterMail);
		verify(dao).findMailsForUser(any(User.class));
		assertThat(newRegisterMail).isExactlyInstanceOf(RegisterMail.class);

		verify(registerMail, times(1)).setState(INACTIVE);
		verify(dao, times(1)).saveRegistrationMail(registerMail);

		verify(inactiveRegisterMail, never()).setState(any(RegisterMailState.class));
	}
	
	@Test
	public void invalidMailingIdShouldReturnNull() {
		//given
		when(dao.findMailByMailingId(anyString())).thenReturn(null);
		
		//when
		RegisterMail registerMailFound = mailService.findMailByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
		assertThat(registerMailFound).isNull();
	}
	
	@Test
	public void findMailByMailingIdWithoutInvalidMailingId() {
		//given
		when(dao.findMailByMailingId(anyString())).thenReturn(registerMail);
		
		//when
		RegisterMail registerMailFound = mailService.findMailByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
		assertThat(registerMailFound).isEqualTo(registerMail);
	}
	
	@Test
	public void findMailByConfirmationId() {
		//given
		when(dao.findMailByConfirmationId(anyString())).thenReturn(registerMail);
		
		//when
		RegisterMail registerMailFound = mailService.findMailByConfirmationId(anyString());
		
		//then
		verify(dao, times(1)).findMailByConfirmationId(anyString());
		assertThat(registerMailFound).isEqualTo(registerMail);
	}
	
	@Test
	public void findUserByMailingIdWithoutInvalidMailingIg() {
		//given
		registerMail.setUser(user);
		when(dao.findMailByMailingId(anyString())).thenReturn(registerMail);
		
		//when
		User userFound = mailService.findUserByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
		assertThat(userFound).isEqualTo(user);
	}
	
	@Test
	public void findUserByConfirmationId() {
		//given
		registerMail.setUser(user);
		when(dao.findMailByConfirmationId(anyString())).thenReturn(registerMail);
		
		//when
		User userFound = mailService.findUserByConfirmationId(anyString());
		
		//then
		verify(dao, times(1)).findMailByConfirmationId(anyString());
		assertThat(userFound).isEqualTo(user);
	}
	
	@Test
	public void isRegisterMailActive() {
		//given
		registerMail.setState(ACTIVE);
		
		//when
		Boolean isRegisterMailActive = mailService.isRegisterMailActive(registerMail);
		
		//then
		assertTrue(isRegisterMailActive);
	}
	
	@Test
	public void isRegisterMailInactive() {
		//given
		registerMail.setState(INACTIVE);
		
		//when
		Boolean isRegisterMailActive = mailService.isRegisterMailActive(registerMail);
		
		//then
		assertFalse(isRegisterMailActive);
	}
}
