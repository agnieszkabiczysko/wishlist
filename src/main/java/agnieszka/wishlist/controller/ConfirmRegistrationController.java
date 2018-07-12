package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import agnieszka.wishlist.model.Password;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.PasswordService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

@Controller
public class ConfirmRegistrationController {

	private static final String SET_PASSWORD_VIEW = "setPassword";
	private static final String REDIRECT_TO_OFFERS = "redirect:/offers";
	private static final String ERROR_VIEW = "error";
	
	@Value("${invalidConfirmatonId}")
	private String invalidConfirmationIdMessage;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegisterMailService registerMailService;
	
	@Autowired
	private PasswordService passwordService;
	
	@RequestMapping(value = "/confirm/{confirmationId}")
	public String confirmRegistration(
			ModelMap model,
			@PathVariable("confirmationId") String confirmationId,
			@ModelAttribute("userPassword") Password userPassword)
	{
		RegisterMail registerMail = registerMailService.findMailByConfirmationId(confirmationId);
		
		if (registerMail == null) {
			return showInvalidMailingIdView(model);
		}
		
		if (!registerMail.isActive()) {
			return handleInactiveMail(model, registerMail);
		}
		
		return SET_PASSWORD_VIEW;
	}

	@RequestMapping(value = "/confirm/{confirmationId}", method = POST)
	public String registerUserConfirmed(
			ModelMap model,
			@PathVariable("confirmationId") String confirmationId,
			@ModelAttribute("userPassword") @Valid Password userPassword,
			BindingResult result)
	{
		User user = findUserBy(confirmationId);
		
		if (user == null) {
			return showInvalidMailingIdView(model);
		}
		
		if (result.hasErrors()) {
			return SET_PASSWORD_VIEW;
		}

		if (!userPassword.isValid()) {
			model.addAttribute("error", "true");
			return SET_PASSWORD_VIEW;
		}

		String passwordHash = hashPassword(userPassword);
		
		setPasswordAndActivate(user, passwordHash);
		
		return REDIRECT_TO_OFFERS;
	}

	private String showInvalidMailingIdView(ModelMap model) {
		model.addAttribute("message", invalidConfirmationIdMessage);
		return ERROR_VIEW;
	}
		
	private String handleInactiveMail(ModelMap model, RegisterMail registerMail) {
		model.addAttribute("inactiveMail", true);
		model.addAttribute("registerMail", registerMail);
		
		return redirectToEmailInactivePage(registerMail);
	}

	private String redirectToEmailInactivePage(RegisterMail registerMail) {
		return "redirect:/emailSent/" + registerMail.getConfirmationId();
	}
	
	private User findUserBy(String mailingId) {
		return registerMailService.findUserByConfirmationId(mailingId);
	}
	
	private String hashPassword(Password userPassword) {
		return passwordService.createPasswordHash(userPassword);
	}

	private void setPasswordAndActivate(User user, String passwordHash) {
		userService.setPasswordAndActivateUser(user, passwordHash);
	}

}
