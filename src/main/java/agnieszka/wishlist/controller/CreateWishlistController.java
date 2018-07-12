package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class CreateWishlistController {

	private static final String REDIRECT_TO_MY_WISHLISTS = "redirect:/myWishlists";
	private static final String CREATE_WISHLIST_VIEW = "createWishlist";

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/createWishlist")
	public String showCreateWishlistForm(ModelMap model, @ModelAttribute Wishlist wishlist) {
		model.addAttribute("wishlist", wishlist);
		return CREATE_WISHLIST_VIEW;
	}

	@RequestMapping(value = "/createWishlist", method = POST)
	public String processCreateWishlistForm(@ModelAttribute Wishlist wishlist, ModelMap model, Principal principal) {
		User wisher = currentUser(principal);
		
		wishlist.setWisher(wisher);
		
		save(wishlist);
		
		return REDIRECT_TO_MY_WISHLISTS;
	}

	private void save(Wishlist wishlist) {
		wishlistService.save(wishlist);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}

}
