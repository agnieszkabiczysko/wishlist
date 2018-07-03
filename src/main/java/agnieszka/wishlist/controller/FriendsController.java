package agnieszka.wishlist.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;

@Controller
public class FriendsController {

	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = {"/myFriends"})
	public String friendsList(ModelMap model, Principal principal) {
		User currentUser = currentUserHelper.getCurrentUser(principal);
		model.addAttribute("friends", currentUser.getFriends()); 
		return "friends"; 
	}
}
