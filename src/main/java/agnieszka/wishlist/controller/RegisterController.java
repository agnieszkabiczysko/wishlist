package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.model.ConfirmationMail;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserProfile;
import agnieszka.wishlist.service.ConfirmationMailService;
import agnieszka.wishlist.service.RegisterMailService;
import agnieszka.wishlist.service.UserService;

@Controller
public class RegisterController {

	private static final String USER_REGISTRATION_FORM_VIEW = "userRegistration";
	private static final String EMAIL_SENT_VIEW = "emailSent";

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegisterMailService registerMailService;
	
	@Autowired
	private ConfirmationMailService confirmationMailService;
	
	@Autowired
	private ApplicationMailer mailer;
	
	@RequestMapping(value = "/register")
	public String showRegisterForm(ModelMap model, @ModelAttribute("user") User user) {
		model.addAttribute("user", user);
		
		return USER_REGISTRATION_FORM_VIEW;
	}
	
	@RequestMapping(value = "/register", method = POST)
	public String registerUser(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return USER_REGISTRATION_FORM_VIEW;
		}
		
		if (userNameAlreadyExists(user.getUserId())) {
			model.addAttribute("userNameAlreadyExists", "true");
			return USER_REGISTRATION_FORM_VIEW;
		}
		
		persist(user);
		
		RegisterMail registerMail = sendAndRecordEmail(user);
		
		model.addAttribute("user", user);
		
		return redirectToEmailSentPage(registerMail);
	}

	@RequestMapping(value = "/emailSent/{mailingId}")
	public String emailSent() {
		return EMAIL_SENT_VIEW;
	}
	
	@RequestMapping(value = "/emailSent/{mailingId}", method = POST)
	public String sendMailAgain(ModelMap model, @PathVariable("mailingId") String mailingId) {
		User user = findUserByMailingId(mailingId);
		
		RegisterMail registerMail = sendAndRecordEmail(user);
		
		return redirectToEmailSentPage(registerMail);
	}

	private boolean userNameAlreadyExists(String userId) {
		return userService.userIdExists(userId);
	}
	
	private void persist(User user) {
		user.addUserProfile(new UserProfile());
		userService.save(user);
	}

	private RegisterMail sendAndRecordEmail(User user) {
		RegisterMail registerMail = recordEmail(user);
		
		ConfirmationMail email = prepareEmail(registerMail);
		
		sendRegisterEmailTo(user, email);
		
		return registerMail;
	}

	private RegisterMail recordEmail(User user) {
		return registerMailService.recordRegisterMail(user);
	}

	private ConfirmationMail prepareEmail(RegisterMail registerMail) {
		return confirmationMailService.prepareMail(registerMail);
	}

	private String redirectToEmailSentPage(RegisterMail registerMail) {
		return "redirect:/emailSent/" + registerMail.getMailingId();
	}

	private User findUserByMailingId(String mailingId) {
		return registerMailService.findUserByMailingId(mailingId);
	}

	private void sendRegisterEmailTo(User user, ConfirmationMail email) {
		mailer.sendMessage(user.getEmail(), email.getTitle(), email.getBody());
	}
	
}
