package test.agnieszka.wishlist.unit.controller;

import static agnieszka.wishlist.model.RegisterMailState.ACTIVE;
import static agnieszka.wishlist.model.RegisterMailState.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.controller.ConfirmRegistrationController;
import agnieszka.wishlist.model.Password;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.PasswordService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmRegistrationControllerTest {

	@InjectMocks
	private ConfirmRegistrationController controller;
	
	@Mock
	private RegisterMailService registerMailService;
	
	@Mock
	private PasswordService passwordService;
	
	@Mock
	private UserService userService;
	
	@Spy
	private ModelMap model;
	
	@Test
	public void showErrorForInvalidConfirmationId() {
		//given
		String invalidConfirmationId = "xyz";
		String errorMessage = "error message";
		
		when(registerMailService.findMailByConfirmationId(invalidConfirmationId)).thenReturn(null);
		
		setField(controller, "invalidConfirmationIdMessage", errorMessage);
		
		//when
		String view = controller.confirmRegistration(model, invalidConfirmationId, null);
		
		//then
		assertThat(view).isEqualTo("error");
		assertThat(model.get("message")).isEqualTo(errorMessage);
	}
	
	@Test
	public void redirectToMailInactivePageForInactiveMail() {
		//given
		String inactiveConfirmationId = "abc";
		String confirmationId = "def";
		RegisterMail inactiveMail = new RegisterMail(null, inactiveConfirmationId, confirmationId, 0L, INACTIVE);
		
		when(registerMailService.findMailByConfirmationId(inactiveConfirmationId)).thenReturn(inactiveMail);
		
		//when
		String view = controller.confirmRegistration(model, inactiveConfirmationId, null);
		
		//then
		assertThat(view).startsWith("redirect:/emailSent/").endsWith(confirmationId);
		assertThat(model.get("registerMail")).isEqualTo(inactiveMail);
		assertThat(model.get("inactiveMail")).isEqualTo(true);
	}
	
	@Test
	public void showSetPasswordViewForActiveMail() {
		//given
		String activeConfirmationId = "mno";
		RegisterMail activeMail = new RegisterMail(null, activeConfirmationId, null, 0L, ACTIVE);
		
		when(registerMailService.findMailByConfirmationId(activeConfirmationId)).thenReturn(activeMail);
		
		//when
		String view = controller.confirmRegistration(model, activeConfirmationId, null);
		
		//then
		assertThat(view).isEqualTo("setPassword");
	}
	
	@Test
	public void showErrorWhenSettingPasswordForInvalidMailId() {
		//given
		String invalidConfirmationId = "pqr";
		String errorMessage = "error error";
		
		when(registerMailService.findUserByConfirmationId(invalidConfirmationId)).thenReturn(null);
		
		setField(controller, "invalidConfirmationIdMessage", errorMessage);

		//when
		String view = controller.registerUserConfirmed(model, invalidConfirmationId, null, null);
		
		//then
		assertThat(view).isEqualTo("error");
		assertThat(model.get("message")).isEqualTo(errorMessage);
	}
	
	@Test
	public void showSetPasswordViewOnValidationErrors() {
		//given
		String confirmationId = "stu";
		User user = user();
		
		BindingResult result = mock(BindingResult.class);
		
		when(registerMailService.findUserByConfirmationId(confirmationId)).thenReturn(user);
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String view = controller.registerUserConfirmed(model, confirmationId, null, result);
		
		//then
		assertThat(view).isEqualTo("setPassword");
	}
	
	@Test
	public void showSetPasswordViewOnInvalidPassword() {
		String confirmationId = "vwx";
		User user = user();
		
		BindingResult result = mock(BindingResult.class);
		
		Password password = mock(Password.class);

		when(registerMailService.findUserByConfirmationId(confirmationId)).thenReturn(user);
		when(result.hasErrors()).thenReturn(false);
		when(password.isValid()).thenReturn(false);
		
		//when
		String view = controller.registerUserConfirmed(model, confirmationId, password, result);
		
		//then
		assertThat(view).isEqualTo("setPassword");
		assertThat(model.get("error")).isEqualTo("true");
	}
	
	@Test
	public void successfulActivation() {
		String confirmationId = "123";
		User user = user();
		
		BindingResult result = mock(BindingResult.class);
		
		Password password = mock(Password.class);

		String passwordHash = "passwordhash";

		when(registerMailService.findUserByConfirmationId(confirmationId)).thenReturn(user);
		when(result.hasErrors()).thenReturn(false);
		when(password.isValid()).thenReturn(true);
		when(passwordService.createPasswordHash(password)).thenReturn(passwordHash);
		
		//when
		String view = controller.registerUserConfirmed(model, confirmationId, password, result);
		
		//then
		assertThat(view).isEqualTo("redirect:/offers");
		
		verify(passwordService, times(1)).createPasswordHash(password);
		verify(userService, times(1)).setPasswordAndActivateUser(user, passwordHash);
	}
	
	private User user() {
		User user = new User();
		user.setUserId("user");
		return user;
	}
	
}
