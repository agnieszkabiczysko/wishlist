package agnieszka.wishlist.controller;

import java.io.File;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.multipart.MultipartFile;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.SearchType;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.WishlistService;

@Controller
@RequestMapping("/")
public class OfferController {
	
	private static final Comparator<Offer> BY_NAME = 
			(Offer o1, Offer o2) -> o1.getName().compareTo(o2.getName());
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;

	@Autowired
	private WishlistService wishlistService;
	
	@Autowired
	private UserPreferencesService preferencesService;
	
	@Value("${imagesRootDirectory}")
	private String imagesRootDirectory;

	@RequestMapping(value = { "/all", "/", "/list", "/offers" })
	public String showAllOffers(ModelMap model) {
		List<Offer> offers = offerService.getAllOffers();
		Collections.sort(offers, BY_NAME);
		model.addAttribute("offers", offers);
		return "offers";
	}
	
	@RequestMapping(value = { "/offer", "/offers/offer" })
	public String showOffer(ModelMap model, @RequestParam("id") int id, Principal principal) {
		Offer offer = offerService.findOfferById(id);
		model.addAttribute("offer", offer);
		User currentUser = currentUserHelper.getCurrentUser(principal);
		if (currentUser != null) {
			Set<Wishlist> wishlists = wishlistService.getAllWishlistsForUser(currentUser);
			model.addAttribute("wishlists", wishlists);
			model.addAttribute("canManage", currentUser.equals(offer.getOfferSeller()));
			model.addAttribute("userHasCurrentWishlist", preferencesService.userHasCurrentWishlist(currentUser));
		}
		return "offer";
	}
	
	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddNewOfferForm(ModelMap model, @ModelAttribute("offer") Offer offer) {
		model.addAttribute("offer", offer);
		return "addNewOffer";
	}
	
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	public String processAddNewOfferForm(@ModelAttribute("offer") @Valid Offer offerToBeAdded,
			BindingResult result, HttpServletRequest request, Principal principal) {
		if (result.hasErrors()) {
			return "addNewOffer";
		}
		User offerSeller = currentUserHelper.getCurrentUser(principal);
		offerToBeAdded.setOfferSeller(offerSeller);
		offerService.save(offerToBeAdded);
		MultipartFile offerImage = offerToBeAdded.getImage();
		saveOfferImage(offerToBeAdded, request, offerImage);
		return "redirect:/offers";
	}
	
	@RequestMapping(value = {"/edit/{id}"}, method=RequestMethod.GET)
	public String editOffer(@PathVariable int id, ModelMap model, Principal principal) {
		Offer offer = offerService.findOfferById(id);
		User currentUser = currentUserHelper.getCurrentUser(principal);
		if (!currentUser.equals(offer.getOfferSeller())) {
			return "redirect:/lackOfPermission";
		}
		model.addAttribute("offer", offer);
		model.addAttribute("edit", true);
		return "addNewOffer";
	}
	
	@RequestMapping(value = {"/edit/{id}"}, method=RequestMethod.POST)
	public String updateOffer(@PathVariable int id, ModelMap model, @Valid Offer offer,
			BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "addNewOffer";
		}
		offerService.update(offer);
		MultipartFile offerImage = offer.getImage();
		saveOfferImage(offer, request, offerImage);
		return "redirect:/offers";
	}

	
	@RequestMapping(value = {"/searchOffer"}, method=RequestMethod.GET)
	public String showSearchOfferForm() {
		return "findOffer";
	}
	
	@RequestMapping(value = {"/searchOffer"}, method=RequestMethod.GET, params = {"searchFor", "searchType"})
	public String showSearchOfferResults(ModelMap model, @RequestParam String searchFor, @RequestParam String searchType) {
		List<Offer> offers = findByNameOrVendor(searchFor, SearchType.valueOf(searchType.toUpperCase()));
		model.addAttribute("offers", offers);
		return "offers";
	}
	
	@RequestMapping(value = {"/lackOfPermission"}, method = RequestMethod.GET)
	public String accessDenied() {
		return "lackOfPermission";
	}
			
	private String getImageFilePath(Offer offer) {
		return imagesRootDirectory + "/" + offer.getId() + ".png";
	}
	
	private void saveOfferImage(Offer offerToBeAdded, HttpServletRequest request, MultipartFile offerImage) {
		if (offerImage != null && !offerImage.isEmpty()) {
			String imagePath = getImageFilePath(offerToBeAdded);
			try {
				offerImage.transferTo(new File(imagePath));
			} catch (Exception e) {
				throw new RuntimeException("Niepowodzenie podczas próby zapisu obrazka", e);
			}
		}
	}
	
	private List<Offer> findByNameOrVendor(String searchFor, SearchType searchType) {
		List<Offer> offers;
		if (searchType == SearchType.NAME) {
			offers = offerService.findOfferByName(searchFor);
		} else if (searchType == SearchType.VENDOR) {
			offers = offerService.findOfferByVendor(searchFor);
		} else {
			offers = null;
		}
		return offers;
	}
}
