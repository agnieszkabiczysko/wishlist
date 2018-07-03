package agnieszka.wishlist.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.exception.InvalidRegisterMailIdException;
import agnieszka.wishlist.model.Password;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.RegisterMailState;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.PasswordService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegisterMailService registerService;
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private ApplicationMailer mailer;
	
	@Autowired
	private UrlHelper urlHelper;
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegisterForm(ModelMap model, @ModelAttribute("user") User user) {
		model.addAttribute("user", user);
		return "userRegistration";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(ModelMap model, @ModelAttribute("user") @Valid User user,
			HttpServletRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return "userRegistration";
		}
		
		if (userService.userIdExists(user.getUserId())) {
			model.addAttribute("userNameAlreadyExists", "true");
			return "userRegistration";
		} else {
			userService.save(user);
			userService.saveRoleUserForUser(user);
			
			RegisterMail registerMail = sendRegisterEmail(user, urlHelper.createRegisterUrl(request));
			
			model.addAttribute("user", user);
			return "redirect:/checkmail/"
						+ registerMail.getConfirmationId();
		}
	}
	
	@RequestMapping(value = "/register/{mailingId}", method = RequestMethod.GET)
	public String showSetPasswordForm(ModelMap model, @PathVariable("mailingId") String mailingId,
							@ModelAttribute("userPassword") Password userPassword) {
		try { 
			return generateSetPasswordView(model, mailingId);
		} catch (InvalidRegisterMailIdException e) {
			model.addAttribute("message", "Nieprawidłowy mail id");
			return "error";
		}
	}
		
	@RequestMapping(value = "/register/{mailingId}", method = RequestMethod.POST)
	public String registerUserConfirmed(ModelMap model, @PathVariable("mailingId") String mailingId,
							@ModelAttribute("userPassword") @Valid Password userPassword,
							BindingResult result) {
		try {
			return setPassword(model, mailingId, userPassword, result);
		} catch (InvalidRegisterMailIdException e) {
			model.addAttribute("message", "Nieprawidłowy mail id");
			return "error";
		}
	}

	@RequestMapping(value = "/checkmail/{confirmationId}", method = RequestMethod.GET)
	public String checkMail() {
		return "checkMail";
	}
	
	@RequestMapping(value = "/checkmail/{confirmationId}", method = RequestMethod.POST)
	public String sendMailAgain(ModelMap model, @PathVariable("confirmationId") String confirmationId, HttpServletRequest request) {
		User user = registerService.findUserByConfirmationId(confirmationId);
		RegisterMail registerMail = sendRegisterEmail(user, urlHelper.createRegisterUrl(request));
		return "redirect:/checkmail/"+registerMail.getConfirmationId();
	}

	private RegisterMail sendRegisterEmail(User user, String url) {
		RegisterMail registerMail = registerService.createRegisterMail(user);
		String body = "Jeśli chcesz się zarejestrować, kliknij w poniższych link: \n" 
				+ url + registerMail.getMailingId()
				+ "\n\nMail został wygenerowany automatycznie. Prosimy na niego nie odpowiadać.";
		mailer.sendMessage(user.getEmail(), "Register", body);
		return registerMail;
	}
	
	private String generateSetPasswordView(ModelMap model, String mailingId) throws InvalidRegisterMailIdException {
		RegisterMail registerMail;
		registerMail = registerService.findMailByMailingId(mailingId);
		if (registerMail.getState() == RegisterMailState.ACTIVE) {
			return "setPassword";
		} else {
			model.addAttribute("inactiveMail", true);
			model.addAttribute("registerMail", registerMail);
			return "redirect:/checkmail/"+registerMail.getConfirmationId();
		}
	}
	
	private String setPassword(ModelMap model, String mailingId, Password userPassword, BindingResult result)
			throws InvalidRegisterMailIdException {
		if (result.hasErrors()) {
			return "setPassword";
		}
		if (userPassword.isValid()) {
			userService.setPasswordAndActivateUser(registerService.findUserByMailingId(mailingId),
						passwordService.createPasswordHash(userPassword));
			return "redirect:/offers";
		} else {
			model.addAttribute("error", "true");
			return "setPassword";
		}
	}
	
}
