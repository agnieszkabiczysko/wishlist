package agnieszka.wishlist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.OfferService;

@Controller
public class ModifyOfferController {

	private static final String ACCESS_DENIED_REDIRECT = "redirect:/accessDenied";
	private static final String NEW_OFFER_VIEW = "addNewOffer";
	private static final String OFFERS_REDIRECT = "redirect:/offers";

	@Autowired
	private OfferService offerService;
	
	@Autowired
	private CurrentUserHelper currentUserHelper;
	
	@Value("${imagesRootDirectory}")
	private String imagesRootDirectory;

	@RequestMapping(value = "/add")
	public String showNewOfferForm(ModelMap model, @ModelAttribute("offer") Offer offer) {
		model.addAttribute("offer", offer);
		
		return NEW_OFFER_VIEW;
	}
	
	@RequestMapping(value = "/add", method = POST)
	public String addOffer(@ModelAttribute("offer") @Valid Offer offer, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return NEW_OFFER_VIEW;
		}
		
		User user = currentUser(principal);
		
		offer.setOfferSeller(user);
		
		saveOffer(offer);
		
		return OFFERS_REDIRECT;
	}

	@RequestMapping(value = "/edit/{id}")
	public String editOffer(@PathVariable int id, ModelMap model, Principal principal) {
		User currentUser = currentUser(principal);

		Offer offer = offer(id);
		
		if (!isOwner(currentUser, offer)) {
			return ACCESS_DENIED_REDIRECT;
		}
		
		model.addAttribute("offer", offer);
		model.addAttribute("edit", true);
		
		return NEW_OFFER_VIEW;
	}
	
	@RequestMapping(value = "/edit/{id}", method = POST)
	public String updateOffer(@PathVariable int id, ModelMap model, @Valid Offer offer, BindingResult result) {
		if (result.hasErrors()) {
			return NEW_OFFER_VIEW;
		}

		saveOffer(offer);
		
		return OFFERS_REDIRECT;
	}

	private void saveOffer(Offer offer) {
		offerService.update(offer);
		
		MultipartFile image = offer.getImage();
		if (imageUploaded(image)) {
			saveOfferImage(image, imageFilePathFor(offer));
		}
	}
	
	private boolean imageUploaded(MultipartFile image) {
		return image != null && !image.isEmpty();
	}

	private void saveOfferImage(MultipartFile offerImage, String imagePath) {
		try {
			offerImage.transferTo(new File(imagePath));
		} catch (Exception e) {
			throw new RuntimeException("Niepowodzenie podczas próby zapisu obrazka", e);
		}
	}
	
	private String imageFilePathFor(Offer offer) {
		return imagesRootDirectory + "/" + offer.getId() + ".png";
	}
	
	private Offer offer(int id) {
		return offerService.findOfferById(id);
	}
	
	private boolean isOwner(User currentUser, Offer offer) {
		return currentUser.equals(offer.getOfferSeller());
	}

	private User currentUser(Principal principal) {
		return currentUserHelper.getCurrentUser(principal);
	}
	
}
