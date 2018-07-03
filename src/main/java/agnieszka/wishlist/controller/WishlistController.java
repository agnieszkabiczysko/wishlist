package agnieszka.wishlist.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.exception.InvalidWishlistMailIdException;
import agnieszka.wishlist.formatter.WishFormatter;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.model.WishlistTermType;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistMailService;
import agnieszka.wishlist.service.WishlistService;

@Controller
public class WishlistController {

	@Value("${wishlistMail.subject}")
	private String subject;
	
	private static final Comparator<Wish> WISH_BY_NAME = 
			(Wish w1, Wish w2) -> w1.getOffer().getName().compareTo(w2.getOffer().getName());
			
	private static final Comparator<Wishlist> WISHLIST_BY_NAME = 
			(Wishlist wl1, Wishlist wl2) -> wl1.getName().compareTo(wl2.getName());

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@Autowired
	private WishlistMailService mailService;
	
	@Autowired
	private ApplicationMailer mailer;

	@Autowired
	private WishFormatter formatter;

	@Autowired
	private UrlHelper urlHelper;

	@RequestMapping(value = { "/wishes/{wishlistId}" }, method = RequestMethod.GET)
	public String showAllWishesForWishlist(@PathVariable int wishlistId, ModelMap model,
			@ModelAttribute EmailAddress shareEmail, Principal principal) {
		generateWishesView(wishlistId, model, shareEmail, principal);
		return "wishes";
	}

	@RequestMapping(value = { "/shareWishes/{wishlistId}" }, method = RequestMethod.POST)
	public String shareWishes(@PathVariable int wishlistId, @ModelAttribute @Valid EmailAddress shareEmail,
			BindingResult result, HttpServletRequest request, Principal principal, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("error", true);
			generateWishesView(wishlistId, model, shareEmail, principal);
			return "wishes";
		}
		Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
		model.addAttribute("wishlistId", wishlist.getId());
		sendWishlistMail(shareEmail, wishlist, urlHelper.createWishlistUrl(request));
		User currentUser = currentUserHelper.getCurrentUser(principal);
		User friend = userService.findUserByEmail(shareEmail);
		if (friend != null) {
			currentUser.addFriend(friend);
			userService.update(currentUser);
		}
		return "redirect:/myWishlists";
	}

	@RequestMapping(value = { "/myWishlists" }, method = RequestMethod.GET)
	public String showWishlistsForUser(ModelMap model, Principal principal) {
		User user = currentUserHelper.getCurrentUser(principal);
		Set<Wishlist> wishlists = sortedWishlistsForUser(user);
		model.addAttribute("wishlists", wishlists);
		return "wishlists";
	}

	@RequestMapping(value = {"/wishlists"}, method = RequestMethod.GET, params = { "userId" })
	public String showWishlistsForSelectedUser(ModelMap model, @RequestParam int userId, Principal principal) {
		User currentUser = currentUserHelper.getCurrentUser(principal);
		User wisher = userService.findUserById(userId);
		Set<Wishlist> wishlists = wishlistService
				.sharedAndPublicWishlistsForUser(wisher, currentUser);
		model.addAttribute("wishlists", wishlists);
		model.addAttribute("search", true);
		model.addAttribute("user", wisher.getUserId());
		return "wishlists";
	}

	@RequestMapping(value = { "/selectWishlist/{offerId}" }, method = RequestMethod.POST, params = { "wishlistId" })
	public String chooseWishlist(@PathVariable int offerId, 
			@RequestParam int wishlistId, Principal principal) {
		Wish wish = new Wish(offerService.findOfferById(offerId));
		Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
		User user = currentUserHelper.getCurrentUser(principal);
		userService.setCurrentWishlist(user, wishlist);
		wishlist.getWishes().add(wish);
		wishlistService.update(wishlist);
		return "redirect:/myWishlists";
	}

	@RequestMapping(value = { "/markWish/{offerId}" })
	public String markOfferAsWish(@PathVariable int offerId, Principal principal) {
		User wisher = currentUserHelper.getCurrentUser(principal);
		Wishlist wishlist = userService.getCurrentWishlist(wisher);
		Wish wish = new Wish(offerService.findOfferById(offerId));
		wishlist.getWishes().add(wish);
		wishlistService.update(wishlist);
		return "redirect:/myWishlists";
	}

	@RequestMapping(value = { "/createWishlist" }, method = RequestMethod.GET)
	public String showCreateWishlistForm(ModelMap model, @ModelAttribute Wishlist wishlist) {
		model.addAttribute("wishlist", wishlist);
		return "createWishlist";
	}

	@RequestMapping(value = { "/createWishlist" }, method = RequestMethod.POST)
	public String processCreateWishlistForm(@ModelAttribute Wishlist wishlist, Principal principal, ModelMap model) {
		User wisher = currentUserHelper.getCurrentUser(principal);
		wishlist.setWisher(wisher);
		wishlistService.save(wishlist);
		Set<Wishlist> wishlists = sortedWishlistsForUser(wisher);
		model.addAttribute("wishlists", wishlists);
		return "wishlists";
	}

	@RequestMapping(value = { "/searchWishlist" }, method = RequestMethod.GET)
	public String showSearchWishlistForm() {
		return "findWishlist";
	}

	@RequestMapping(value = { "/searchWishlist" }, method = RequestMethod.GET, params = { "searchFor", "searchType" })
	public String showSearchWishlistResults(ModelMap model, @RequestParam String searchFor, @RequestParam String searchType, Principal principal) {
		model.addAttribute("search", true);
		Set<Wishlist> wishlists = (principal != null)
				? getCurrentUserWishlists(model, searchFor, searchType, principal) 
				: getOnlyPublicWishlists(model, searchFor, searchType);
		if (wishlists.isEmpty()) {
			return "noResultsFound";
		}
		return "wishlists";
	}

	@RequestMapping(value =  "/wishlist/{mailId}" , method = RequestMethod.GET)
	public String showWishlistMail(ModelMap model, @PathVariable String mailId) {
		try {
			return generateWishlistViewBasedOnMailId(model, mailId);
		} catch (InvalidWishlistMailIdException e) {
			model.addAttribute("message", "Nieprawidłowy identyfikator wishlisty");
			return "error";
		}
	}
	

	private Set<Wishlist> sortedWishlistsForUser(User user) {
		Set<Wishlist> wishlists = new TreeSet<>(WISHLIST_BY_NAME);
		wishlists.addAll(wishlistService.getAllWishlistsForUser(user));
		return wishlists;
	}

	private String generateWishlistViewBasedOnMailId(ModelMap model, String mailId) throws InvalidWishlistMailIdException {
		Wishlist wishes = mailService.findWishlistById(mailId);
		model.addAttribute("wishes", wishes.getWishes());
		model.addAttribute("user", wishes.getWisher());
		model.addAttribute("isWisher", false);
		model.addAttribute("wishlistIsEmpty", false);
		return "wishes";
	}

	private void generateWishesView(int wishlistId, ModelMap model, EmailAddress shareEmail, Principal principal) {
		Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
		Set<Wish> wishes = new TreeSet<>(WISH_BY_NAME);
		wishes.addAll(wishlist.getWishes());
		model.addAttribute("wishlistIsEmpty", wishlist.isEmpty());
		model.addAttribute("user", wishlist.getWisher());
		model.addAttribute("wishes", wishes);
		model.addAttribute("shareEmail", shareEmail);
		model.addAttribute("isWisher", isCurrentUserAWisher(principal, wishlist));
	}

	private boolean isCurrentUserAWisher(Principal principal, Wishlist wishlist) {
		if (principal == null) {
			return false;
		}
		User currentUser = currentUserHelper.getCurrentUser(principal);
		return currentUser.equals(wishlist.getWisher());
	}
	
	private boolean searchingForOwnWishlists(User currentUser, User wisher) {
		return currentUser.equals(wisher);
	}
	
	private Set<Wishlist> getOnlyPublicWishlists(ModelMap model, String searchFor, String searchType) {
		Set<Wishlist> wishlists = wishlistService.findWishlistsByTermAndState(WishlistTermType.valueOf(searchType), searchFor, WishlistState.PUBLIC);
		wishlists = wishlistsWithoutEmptyWishlist(wishlists);
		model.addAttribute("wishlists", wishlists);
		return wishlists;
	}

	private Set<Wishlist>  wishlistsWithoutEmptyWishlist(Set<Wishlist> wishlists) {
		Set<Wishlist> wishlistWithoutEmptyElement = new HashSet<>();
		for (Wishlist wishlist : wishlists) {
			if ((!wishlist.isEmpty())) {
				wishlistWithoutEmptyElement.add(wishlist);
			}
		}
		return wishlistWithoutEmptyElement;
	}

	private Set<Wishlist> getCurrentUserWishlists(ModelMap model, String searchFor, String searchType,
			Principal principal) {
		User currentUser = currentUserHelper.getCurrentUser(principal);
		Set<Wishlist> wishlists = wishlistService.findWishlistsByTerm(WishlistTermType.valueOf(searchType), searchFor);
		User wisher = userService.findUserByUserId(searchFor);
		if (searchingForOwnWishlists(currentUser, wisher)) {
			model.addAttribute("wishlists", wishlists);
		} else {
			wishlists = wishlistService.sharedAndPublicWishlistsForUser(wisher, currentUser);
			model.addAttribute("wishlists", wishlists);
		}
		model.addAttribute("user", wisher.getUserId());
		return wishlists;
	}
			
	private void sendWishlistMail(EmailAddress shareEmail, Wishlist wishlist, String url) {
		WishlistMail wishlistMail = mailService.createWishlistMail(wishlist);
		String body = "Lista życzeń \n"
				+ "użytkownika: " + wishlist.getWisher().getUserId() + "\n\n"
				+ formatter.formatWishlistForEmail(wishlist.getWishes()) + "\n\n"
				+ "Poniżej link do listy \n"
				+ url + wishlistMail.getMailId()
				+ "\n\nMail został wygenerowany automatycznie. Prosimy na niego nie odpowiadać.";
		mailer.sendMessage(shareEmail, subject, body);
	}
}
