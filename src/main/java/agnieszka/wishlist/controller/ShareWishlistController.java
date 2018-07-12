package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.Principal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.ShareWishlistEmailSendService;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class ShareWishlistController {

	private static final String WISHLIST_VIEW = "wishes";
	private static final String REDIRECT_TO_MY_WISHLISTS = "redirect:/myWishlists";

	private static final Comparator<Wish> WISH_BY_NAME = 
			(Wish w1, Wish w2) -> w1.getOffer().getName().compareTo(w2.getOffer().getName());
			
	@Autowired
	private WishlistService wishlistService;
	
	@Autowired
	private ShareWishlistEmailSendService sendEmailService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@RequestMapping(value = "/shareWishlist/{wishlistId}", method = POST)
	public String shareWishlist(
			@PathVariable int wishlistId,
			@ModelAttribute @Valid EmailAddress emailAddress,
			BindingResult result,
			ModelMap model,
			Principal principal)
	{
		User currentUser = currentUser(principal);
	
		return (result.hasErrors())
			? handleValidationError(wishlistId, model, emailAddress, currentUser)
			: shareWishListAndRedirect(wishlistId, emailAddress, model, currentUser);
	}

	private String handleValidationError(int wishlistId, ModelMap model, EmailAddress emailAddress, User user) {
		Wishlist wishlist = wishlist(wishlistId);
		
		model.addAttribute("error", true);
		model.addAttribute("wishlistIsEmpty", wishlist.isEmpty());
		model.addAttribute("user", wishlist.getWisher());
		model.addAttribute("wishes", sortedWishes(wishlist.getWishes()));
		model.addAttribute("wishlistId", wishlistId);
		model.addAttribute("shareEmail", emailAddress);
		model.addAttribute("isWisher", isWisherOf(user, wishlist));

		return WISHLIST_VIEW;
	}
	
	private String shareWishListAndRedirect(int wishlistId, EmailAddress emailAddress, ModelMap model, User currentUser) {
		Wishlist wishlist = wishlist(wishlistId);
		User friend = findUserByEmailAddress(emailAddress);
		
		shareViaEmail(emailAddress, wishlist);
		makeFriend(currentUser, friend);
		
		return REDIRECT_TO_MY_WISHLISTS;
	}

	private Wishlist wishlist(int wishlistId) {
		return wishlistService.findWishlistById(wishlistId);
	}
	
	private Set<Wish> sortedWishes(Set<Wish> wishes) {
		Set<Wish> sorted = new TreeSet<>(WISH_BY_NAME);
		wishes.addAll(wishes);
		return sorted;
	}

	private boolean isWisherOf(User user, Wishlist wishlist) {
		return (user == null)
			? false
			: user.equals(wishlist.getWisher());
	}
	
	private User findUserByEmailAddress(EmailAddress emailAddress) {
		return userService.findUserByEmail(emailAddress);
	}

	private void shareViaEmail(EmailAddress emailAddress, Wishlist wishlist) {
		sendEmailService.shareWishlistViaEmail(wishlist, emailAddress);
	}

	private void makeFriend(User currentUser, User friend) {
		if (friend != null) {
			currentUser.addFriend(friend);
			userService.update(currentUser);
		}
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}
}
