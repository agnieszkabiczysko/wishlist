package agnieszka.wishlist.controller;

import static java.util.Collections.sort;
import static java.util.Comparator.comparing;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class ShowOfferController {
	
	private static final String OFFERS_VIEW = "offers";
	private static final String OFFER_VIEW = "offer";

	@Autowired
	private WishlistService wishlistService;
	
	@Autowired
	private UserPreferencesService preferencesService;

	@Autowired
	private OfferService offerService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/offers")
	public String showOffers(ModelMap model) {
		List<Offer> offers = offers();
		
		sort(offers, comparing(Offer::getName));
		
		model.addAttribute("offers", offers);
		
		return OFFERS_VIEW;
	}

	@RequestMapping(value = "/offer", params = "id")
	public String showOffer(ModelMap model, @RequestParam("id") int offerId, Principal principal) {
		User currentUser = currentUser(principal);

		return isUserLoggedIn(currentUser)
			? loggedInView(model, offerId, currentUser)
			: anonymousView(model, offerId);
	}

	private List<Offer> offers() {
		return offerService.getOffers();
	}
	
	private Offer offer(int id) {
		return offerService.findOfferById(id);
	}

	private boolean isUserLoggedIn(User currentUser) {
		return currentUser != null;
	}

	private String anonymousView(ModelMap model, int id) {
		model.addAttribute("offer", offer(id));

		return OFFER_VIEW;
	}

	private String loggedInView(ModelMap model, int offerId, User currentUser) {
		model.addAttribute("wishlists", wishlistsOf(currentUser));
		model.addAttribute("canManage", canManage(offerId, currentUser));
		model.addAttribute("userHasCurrentWishlist", hasCurrentWishlist(currentUser));
		model.addAttribute("offer", offer(offerId));

		return OFFER_VIEW;
	}

	private Set<Wishlist> wishlistsOf(User currentUser) {
		return wishlistService.findWishlistsOf(currentUser);
	}

	private boolean canManage(int offerId, User currentUser) {
		return currentUser.equals(offer(offerId).getOfferSeller());
	}

	private boolean hasCurrentWishlist(User currentUser) {
		return preferencesService.userHasCurrentWishlist(currentUser);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}
	
}
