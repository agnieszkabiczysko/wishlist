package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.controller.RegisterController;
import agnieszka.wishlist.model.ConfirmationMail;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.ConfirmationMailService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerTest {

	@InjectMocks
	private RegisterController controller;
	
	@Mock
	private UserService userService;
	
	@Mock
	private RegisterMailService registerMailService;
	
	@Mock
	private ConfirmationMailService confirmationMailService;
	
	@Mock
	private ApplicationMailer applicationMailer;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private BindingResult result;
	
	@Test
	public void showRegisterForm() {
		//given
		User user = user();
		
		//when
		String view = controller.showRegisterForm(model, user);
		
		//then
		assertThat(view).isEqualTo("userRegistration");
		assertThat(model.get("user")).isEqualTo(user);
	}
	
	@Test
	public void showErrorViewOnValidationErrors() {
		//given
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String view = controller.registerUser(model, user(), result);
		
		//then
		assertThat(view).isEqualTo("userRegistration");
	}
	
	@Test
	public void showErrorOnDuplicatedUserId() {
		//given
		User user = user();
		
		when(result.hasErrors()).thenReturn(false);
		when(userService.userIdExists(user.getUserId())).thenReturn(true);
		
		//when
		String view = controller.registerUser(model, user, result);
		
		//then
		assertThat(view).isEqualTo("userRegistration");
		assertThat(model.get("userNameAlreadyExists")).isEqualTo("true");
	}
	
	@Test
	public void successfulRegistration() {
		//given
		User user = user();
		RegisterMail registerMail = registerMail();
		ConfirmationMail confirmationMail = confirmationMail();
		
		when(result.hasErrors()).thenReturn(false);
		when(userService.userIdExists(user.getUserId())).thenReturn(false);
		when(registerMailService.recordRegisterMail(user)).thenReturn(registerMail);
		when(confirmationMailService.prepareMail(registerMail)).thenReturn(confirmationMail);
		
		//when
		String view = controller.registerUser(model, user, result);
		
		//then
		assertThat(view).startsWith("redirect:/emailSent/").endsWith(registerMail.getMailingId());
		assertThat(user.getUserProfiles()).isNotEmpty();
		assertThat(model.get("user")).isEqualTo(user);
		
		verify(userService, times(1)).save(user);
		verify(registerMailService, times(1)).recordRegisterMail(user);
		verify(confirmationMailService, times(1)).prepareMail(registerMail);
		verify(applicationMailer, times(1)).sendMessage(user.getEmail(), confirmationMail.getTitle(), confirmationMail.getBody());
	}
	
	@Test
	public void showEmailSentPage() {
		//when
		String view = controller.emailSent();
		
		//then
		assertThat(view).isEqualTo("emailSent");
	}
	
	@Test
	public void sendMailAgain() {
		//given
		String mailingId = "xyz";
		User user = user();
		RegisterMail registerMail = registerMail();
		ConfirmationMail confirmationMail = confirmationMail();
		
		when(registerMailService.findUserByMailingId(mailingId)).thenReturn(user);
		when(registerMailService.recordRegisterMail(user)).thenReturn(registerMail);
		when(confirmationMailService.prepareMail(registerMail)).thenReturn(confirmationMail);
		
		//when
		String view = controller.sendMailAgain(model, mailingId);
		
		//then
		assertThat(view).startsWith("redirect:/emailSent/").endsWith(registerMail.getMailingId());
		
		verify(registerMailService, times(1)).recordRegisterMail(user);
		verify(confirmationMailService, times(1)).prepareMail(registerMail);
		verify(applicationMailer, times(1)).sendMessage(user.getEmail(), confirmationMail.getTitle(), confirmationMail.getBody());
	}

	private User user() {
		User user = new User();
		user.setUserId("user");
		user.setEmail(new EmailAddress("test@test.com"));
		return user;
	}
	
	private RegisterMail registerMail() {
		RegisterMail mail = new RegisterMail();
		mail.setConfirmationId("abc");
		mail.setMailingId("def");
		return mail;
	}

	private ConfirmationMail confirmationMail() {
		return new ConfirmationMail("title", "body");
	}

}
