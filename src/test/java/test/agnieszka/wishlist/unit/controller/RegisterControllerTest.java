package test.agnieszka.wishlist.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.controller.RegisterController;
import agnieszka.wishlist.exception.InvalidRegisterMailIdException;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Password;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.PasswordService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

public class RegisterControllerTest {

	private static final String MAILING_ID = "2c55bb6d-a12b-4201-ad26-0eb7933be319";
	private static final String CONFIRMATION_ID = "eee90b4b-9b0b-4f2f-8fe2-37085434a3ed";
	
	@Mock
	private UserService userService;
	
	@Mock
	private RegisterMailService registerService;
	
	@Mock
	private PasswordService passwordService;
	
	@Mock
	private ApplicationMailer applicationMailer;
	
	@InjectMocks
	private RegisterController controller;
	
	@Spy
	private ModelMap model;
	
	@Spy
	private User user;
	
	@Spy
	private RegisterMail registerMail;
	
	@Mock
	private BindingResult result;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private UrlHelper urlHelper;
	
	@Mock
	private Password password;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		user = getUser();
		registerMail = getRegisterMail();
	}
	
	@Test
	public void showRegisterForm() {
		//when
		String viewName = controller.showRegisterForm(model, user);
		
		//then
		assertEquals("userRegistration", viewName);
		assertEquals(user, model.get("user"));
	}
	
	@Test
	public void registerUserWithoutSuccess() {
		//given
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.registerUser(model, user, request, result);
		
		//then
		assertEquals("userRegistration", viewName);
	}
	
	@Test
	public void registerUserWithoutUserIdUnique() {
		//given
		when(result.hasErrors()).thenReturn(false);
		when(userService.userIdExists(anyString())).thenReturn(true);
		
		//when
		String viewName = controller.registerUser(model, user, request, result);
		
		//then
		assertEquals("userRegistration", viewName);
		assertEquals("true", model.get("userNameAlreadyExists"));
	}
	
	@Test
	public void registerUserWithSuccess() {
		//given
		when(result.hasErrors()).thenReturn(false);
		when(userService.userIdExists(user.getUserId())).thenReturn(false);
		doNothing().when(userService).save(user);
		doNothing().when(userService).saveRoleUserForUser(user);
		mockSendRegisterMail();
		
		//when
		String viewName = controller.registerUser(model, user, request, result);
		
		//then
		assertEquals("redirect:/checkmail/"+registerMail.getConfirmationId(), viewName);
	}
	
	@Test
	public void showSetPasswordFormForValidMailingId() throws InvalidRegisterMailIdException {
		//given
		when(registerService.findMailByMailingId(MAILING_ID)).thenReturn(registerMail);
		registerMail.setState(RegisterMailState.ACTIVE);
		
		//when
		String viewName = controller.showSetPasswordForm(model, MAILING_ID, password);
		
		//then
		assertEquals("setPassword", viewName);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void showExceptionPageAfterDeliveringInvalidMailingId() throws InvalidRegisterMailIdException {
		//given
		when(registerService.findMailByMailingId("invalidMailingId")).thenThrow(InvalidRegisterMailIdException.class);
		
		//when
		String viewName = controller.showSetPasswordForm(model, "invalidMailingId", password);
		
		//then
		assertEquals("error", viewName);
	}
	
	@Test
	public void chechIfRegisterMailIsNotActive() throws InvalidRegisterMailIdException {
		//given
		when(registerService.findMailByMailingId(anyString())).thenReturn(registerMail);
		when(registerService.isRegisterMailActive(any(RegisterMail.class))).thenReturn(false);
		
		//when
		String viewName = controller.showSetPasswordForm(model, anyString(), password);
		
		//then
		assertEquals("redirect:/checkmail/"+registerMail.getConfirmationId(), viewName);
		assertEquals(true, model.get("inactiveMail"));
		assertEquals(registerMail, model.get("registerMail"));
	}
	
	@Test
	public void registerUserConfirmationWithError() {
		//given
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.registerUserConfirmed(model, registerMail.getConfirmationId(), password, result);
		
		//then
		assertEquals("setPassword", viewName);
	}
	
	
	@Test
	public void registerUserConfirmationWithIncorrectPassword() {
		//given
		when(result.hasErrors()).thenReturn(false);
		when(password.isValid()).thenReturn(false);
		
		//when
		String viewName = controller.registerUserConfirmed(model, registerMail.getConfirmationId(), password, result);
		
		//then
		assertEquals("setPassword", viewName);
		assertEquals("true", model.get("error"));
	}
	
	@Test
	public void registerUserConfirmationWithSuccess() {
		//given
		when(result.hasErrors()).thenReturn(false);
		when(password.isValid()).thenReturn(true);
		doNothing().when(userService).setPasswordAndActivateUser(any(User.class), anyString());
		
		//when
		String viewName = controller.registerUserConfirmed(model, registerMail.getConfirmationId(), password, result);
		
		//then
		assertEquals("redirect:/offers", viewName);
	}
	
	
	@Test
	public void checkMail() {
		//when
		String viewName = controller.checkMail();
				
		//then
		assertEquals("checkMail", viewName);
	}
	
	@Test
	public void sendMailAgain() {
		//given
		when(registerService.findUserByConfirmationId(anyString())).thenReturn(user);
		mockSendRegisterMail();
		
		//when
		String viewName = controller.sendMailAgain(model, anyString(), request);
		
		//then
		assertEquals("redirect:/checkmail/"+registerMail.getConfirmationId(), viewName);
	}

	private void mockSendRegisterMail() {
		when(urlHelper.createRegisterUrl(request)).thenReturn("http://localhost:8080/wishist/register/");
		when(registerService.createRegisterMail(any(User.class))).thenReturn(registerMail);
		doNothing().when(applicationMailer).sendMessage(any(EmailAddress.class), anyString(), anyString());
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");
		EmailAddress mail = new EmailAddress();
		mail.setEmail("test@test");
		user.setEmail(mail);
		return user;
	}
	
	private RegisterMail getRegisterMail() {
		RegisterMail mail = new RegisterMail();
		mail.setConfirmationId(CONFIRMATION_ID);
		mail.setMailingId(MAILING_ID);
		return mail;
	}

}
