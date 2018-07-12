package agnieszka.wishlist.controller;

import java.security.Principal;
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
import agnieszka.wishlist.model.WishlistTermType;
import agnieszka.wishlist.service.UserService;

@Controller
public class SearchWishlistsController {

	private static final String SEARCH_FORM_VIEW = "findWishlist";
	private static final String WISHLISTS_VIEW = "wishlists";
	private static final String NO_RESULTS_VIEW = "noResultsFound";

	@Autowired
	private UserService userService;
	
	@Autowired
	private WishlistsHelper wishlistsHelper;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/searchWishlist")
	public String showSearchForm() {
		return SEARCH_FORM_VIEW;
	}

	@RequestMapping(value = "/searchWishlist", params = { "term", "termTypeString" })
	public String showSearchWishlistResults(ModelMap model, @RequestParam String term, @RequestParam String termTypeString, Principal principal) {
		WishlistTermType termType = WishlistTermType.valueOf(termTypeString);
		
		User owner = findUserByTerm(term, termType);
		
		User currentUser = currentUser(principal);
		
		Set<Wishlist> wishlists = getWishlists(owner, currentUser); 
		
		model.addAttribute("wishlists", wishlists);
		model.addAttribute("search", true);
		model.addAttribute("term", term);

		return wishlists.isEmpty()
				? NO_RESULTS_VIEW
				: WISHLISTS_VIEW;
	}

	private User findUserByTerm(String term, WishlistTermType termType) {
		switch (termType) {
		case USERID:
			return userService.findUserByUserId(term);
		case EMAIL:
			return userService.findUserByEmail(term);
		}
		throw new IllegalArgumentException("Unknown search type: " + termType);
	}
	
	private Set<Wishlist> getWishlists(User owner, User currentUser) {
		return wishlistsHelper.wishlistsOfVisibleFor(owner, currentUser);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}

}
