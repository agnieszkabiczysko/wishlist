package agnieszka.wishlist.controller;

import static java.util.Collections.sort;
import static java.util.Comparator.comparing;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.controller.helper.WishlistsHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.UserService;

@Controller
public class ShowWishlistsController {

	private static final String VIEW_NAME = "wishlists";

	@Autowired
	private UserService userService;
	
	@Autowired
	private WishlistsHelper wishlistsHelper;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/myWishlists")
	public String showWishlistsForUser(ModelMap model, Principal principal) {
		User currentUser = currentUser(principal);
		
		List<Wishlist> wishlists = new ArrayList<>(wishlistsOf(currentUser));
		sort(wishlists, comparing(Wishlist::getName));
		
		model.addAttribute("wishlists", wishlists);
		
		return VIEW_NAME;
	}

	@RequestMapping(value = "/wishlists", params = "userId")
	public String showWishlistsForSelectedUser(ModelMap model, @RequestParam int userId, Principal principal) {
		User currentUser = currentUser(principal);
		
		User owner = user(userId);
		Set<Wishlist> wishlists = getWishlists(owner, currentUser);
		
		model.addAttribute("wishlists", wishlists);
		model.addAttribute("search", true);
		model.addAttribute("user", owner.getUserId());
		
		return VIEW_NAME;
	}

	private Set<Wishlist> wishlistsOf(User owner) {
		return wishlistsHelper.wishlistsOf(owner);
	}

	private Set<Wishlist> getWishlists(User owner, User currentUser) {
		return wishlistsHelper.wishlistsOfVisibleFor(owner, currentUser);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}

	private User user(int userId) {
		return userService.findUserById(userId);
	}

}
