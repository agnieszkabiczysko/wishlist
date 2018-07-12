package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class AddToWishlistController {

	private static final String REDIRECT_TO_MY_WISHLISTS = "redirect:/myWishlists";

	private static final int ADD_TO_CURRENT_WISHLIST = 0;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/addToCurrentWishlist/{offerId}")
	public String addOfferToCurrentWishlist(@PathVariable int offerId, Principal principal) {
		return addOfferToWishlist(offerId, ADD_TO_CURRENT_WISHLIST, principal);
	}

	@RequestMapping(value = "/addOfferToWishlist/{offerId}", method = POST, params = "wishlistId")
	public String addOfferToWishlist(@PathVariable int offerId, @RequestParam int wishlistId, Principal principal) {
		User user = currentUser(principal);
		Wishlist wishlist = getWishlist(wishlistId, user);
		Offer offer = offer(offerId);
		
		addToWishlist(offer, wishlist, user);

		return REDIRECT_TO_MY_WISHLISTS;
	}

	private Wishlist getWishlist(int wishlistId, User user) {
		return (wishlistId == ADD_TO_CURRENT_WISHLIST)
				? currentWishlistOf(user)
				: findWishlistById(wishlistId);
	}

	private Wishlist currentWishlistOf(User currentUser) {
		return wishlistService.getCurrentWishlistOf(currentUser);
	}

	private Wishlist findWishlistById(int wishlistId) {
		return wishlistService.findWishlistById(wishlistId);
	}

	private Offer offer(int offerId) {
		return offerService.findOfferById(offerId);
	}

	private void addToWishlist(Offer offer, Wishlist wishlist, User user) {
		wishlist.add(offer);
		
		update(wishlist);
		
		setCurrentWishlistFor(wishlist, user);
	}

	private void update(Wishlist wishlist) {
		wishlistService.update(wishlist);
	}

	private void setCurrentWishlistFor(Wishlist wishlist, User user) {
		wishlistService.setCurrentWishlistOf(user, wishlist);
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}

}
