package agnieszka.wishlist.controller;

import static java.util.Collections.sort;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class ShowWishlistController {

	private static final String WISHLIST_VIEW = "wishes";

	private static final Comparator<Wish> BY_NAME = 
			(Wish w1, Wish w2) -> w1.getOffer().getName().compareTo(w2.getOffer().getName());

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = { "/wishlist/{id}" }, method = RequestMethod.GET)
	public String showWishlist(@PathVariable int id, @ModelAttribute EmailAddress shareEmail, ModelMap model, Principal principal) {
		Wishlist wishlist = wishlist(id);
		List<Wish> wishes = sortedWishesFrom(wishlist);
		
		model.addAttribute("wishlistIsEmpty", wishlist.isEmpty());
		model.addAttribute("user", wishlist.getWisher());
		model.addAttribute("wishes", wishes);
		model.addAttribute("isWisher", isOwner(currentUser(principal), wishlist));
		model.addAttribute("wishlistId", id);
		model.addAttribute("shareEmail", shareEmail);
	
		return WISHLIST_VIEW;
	}

	private ArrayList<Wish> sortedWishesFrom(Wishlist wishlist) {
		ArrayList<Wish> wishes = new ArrayList<>(wishlist.getWishes());
		sort(wishes, BY_NAME);
		return wishes;
	}

	private Wishlist wishlist(int wishlistId) {
		return wishlistService.findWishlistById(wishlistId);
	}
	
	private boolean isOwner(User user, Wishlist wishlist) {
		return wishlist.getWisher().equals(user);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}
}
