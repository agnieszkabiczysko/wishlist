package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;

import agnieszka.wishlist.dao.RegisterMailDao;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.exception.InvalidRegisterMailIdException;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.RegisterMailServiceImpl;

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

	private List<RegisterMail> registerMailList = new ArrayList<>();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		registerMailList.add(registerMail);
	}
	
	@Test
	public void saveRegistrationMail() {
		//given
		doNothing().when(dao).saveRegistrationMail(any(RegisterMail.class));
		
		//when
		mailService.saveRegistrationMail(any(RegisterMail.class));
		
		//then
		verify(dao, times(1)).saveRegistrationMail(any(RegisterMail.class));
	}
	
	@Test
	public void createRegisterMail() {
		//given
		doNothing().when(userDao).update(user);
		doNothing().when(dao).saveRegistrationMail(any(RegisterMail.class));
		when(dao.findMailsForUser(any(User.class))).thenReturn(registerMailList);
		
		//when
		RegisterMail foundedRegisterMail = mailService.createRegisterMail(any(User.class));
		
		//then
		verify(dao, times(1)).saveRegistrationMail(any(RegisterMail.class));
		verify(dao).findMailsForUser(any(User.class));
		assertThat(foundedRegisterMail).isExactlyInstanceOf(RegisterMail.class);
	}
	
	@Test(expected = InvalidRegisterMailIdException.class)
	public void invalidMailingIdShouldThrowException() throws InvalidRegisterMailIdException {
		//given
		when(dao.findMailByMailingId(anyString())).thenReturn(null);
		
		//when
		mailService.findMailByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
	}
	
	@Test
	public void findMailByMailingIdWithoutInvalidMailingId() throws InvalidRegisterMailIdException {
		//given
		when(dao.findMailByMailingId(anyString())).thenReturn(registerMail);
		
		//when
		RegisterMail foundedMail = mailService.findMailByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
		assertThat(foundedMail).isEqualTo(registerMail);
	}
	
	@Test
	public void findMailByConfirmationId() {
		//given
		when(dao.findMailByConfirmationId(anyString())).thenReturn(registerMail);
		
		//when
		RegisterMail foundedMail = mailService.findMailByConfirmationId(anyString());
		
		//then
		verify(dao, times(1)).findMailByConfirmationId(anyString());
		assertThat(foundedMail).isEqualTo(registerMail);
	}
	
	@Test
	public void findUserByMailingIdWithoutInvalidMailingIg() throws InvalidRegisterMailIdException {
		//given
		registerMail.setUser(user);
		when(dao.findMailByMailingId(anyString())).thenReturn(registerMail);
		
		//when
		User foundedUser = mailService.findUserByMailingId(anyString());
		
		//then
		verify(dao, times(1)).findMailByMailingId(anyString());
		assertThat(foundedUser).isEqualTo(user);
	}
	
	@Test
	public void findUserByConfirmationId() {
		//given
		registerMail.setUser(user);
		when(dao.findMailByConfirmationId(anyString())).thenReturn(registerMail);
		
		//when
		User foundedUser = mailService.findUserByConfirmationId(anyString());
		
		//then
		verify(dao, times(1)).findMailByConfirmationId(anyString());
		assertThat(foundedUser).isEqualTo(user);
	}
	
	@Test
	public void isRegisterMailActive() {
		//given
		registerMail.setState(RegisterMailState.ACTIVE);
		
		//when
		Boolean isRegisterMailActive = mailService.isRegisterMailActive(registerMail);
		
		//then
		assertTrue(isRegisterMailActive);
	}
	
	@Test
	public void isRegisterMailInactive() {
		//given
		registerMail.setState(RegisterMailState.INACTIVE);
		
		//when
		Boolean isRegisterMailActive = mailService.isRegisterMailActive(registerMail);
		
		//then
		assertFalse(isRegisterMailActive);
	}
}
