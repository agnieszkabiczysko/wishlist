package agnieszka.wishlist.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.service.WishService;

@Controller
public class WishController {

	private static final String WISH_VIEW = "wish";
	private static final String REDIRECT_TO_OFFERS = "redirect:/offers";

	@Autowired
	private WishService wishService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/wish/{wishId}")
	public String showWish(ModelMap model, @PathVariable int wishId, Principal principal) {
		Wish wish = wish(wishId);
		
		model.addAttribute("wish", wish);
		model.addAttribute("isPurchased", wish.isPurchased());
		model.addAttribute("canBuy", !isWisherOf(currentUser(principal), wish));
		
		return WISH_VIEW;
	}

	@RequestMapping(value = {"/fulfil/{id}"}, method = RequestMethod.POST)
	public String fulfilWish(@PathVariable int wishId, Principal principal) {
		User fulfiller = currentUser(principal);
		Wish wish = wish(wishId);
		
		wishService.fulfilWish(wish, fulfiller);
		
		return REDIRECT_TO_OFFERS;
	}

	private boolean isWisherOf(User user, Wish wish) {
		return wish.getWishlist().getWisher().equals(user);
	}

	private Wish wish(int wishId) {
		return wishService.findWishById(wishId);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}
}
