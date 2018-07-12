package agnieszka.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {
	
	private static final String LOGIN_VIEW = "login";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return LOGIN_VIEW;
	}
	
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginError(ModelMap model) {
		model.addAttribute("error", "true");
		
		return LOGIN_VIEW;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		model.addAttribute("logout", true);
		
		return LOGIN_VIEW;
	}
}
