package agnieszka.wishlist.controller.helper;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.UserService;

@Component
public class CurrentUserHelper {

	@Autowired
	private UserService userService;
	
	public User getCurrentUser(Principal principal) {
		if (principal == null) {
			return null;
		}
		return userService.getCurrentUser(principal.getName());
	}
}
