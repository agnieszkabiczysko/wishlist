package agnieszka.wishlist.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishService;

@Controller
public class WishController {

	@Autowired
	private WishService wishService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = {"/wishes"})
	public String showAllWishes(ModelMap model) {
		List<Wish> wishes = wishService.getAllWishes();
		model.addAttribute("wishes", wishes);
		return "wishes";
	}

	@RequestMapping(value = {"/wish"})
	public String showWish(ModelMap model, @RequestParam int wisherId, @RequestParam int user, Principal principal) {
		if (principal != null) {
			User currentUser = currentUserHelper.getCurrentUser(principal);
			User wisher = userService.findUserById(user);
			model.addAttribute("myWish", currentUser.equals(wisher));
		}
		Wish wish = wishService.findWishById(wisherId);
		model.addAttribute("wish", wish);
		model.addAttribute("purchased", wishService.isWishPurchased(wish));
		return "wish";
	}

	@RequestMapping(value = {"/buy/{id}"}, method = RequestMethod.POST)
	public String wantToBuy(@PathVariable int id, Principal principal) {
		User fulfiller = currentUserHelper.getCurrentUser(principal);
		Wish wish = wishService.findWishById(id);
		wishService.fulfilWish(wish, fulfiller);
		return "redirect:/offers";
	}
}
