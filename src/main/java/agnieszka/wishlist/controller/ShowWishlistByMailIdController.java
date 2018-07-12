package agnieszka.wishlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistMailService;

@Controller
public class ShowWishlistByMailIdController {
	private static final String WISHLIST_VIEW = "wishes";
	private static final String ERROR_VIEW = "error";
	
	@Value("${invalidMailId}")
	private String invalidMailIdMessage;

	@Autowired
	private WishlistMailService mailService;
	
	@RequestMapping(value = "/wishlist", params = "mailId")
	public String showWishlist(ModelMap model, @RequestParam String mailId) {
		Wishlist wishlist = findWishlistByMailId(mailId);
		
		return (wishlist == null)
				? handleMailIdNotFound(model)
				: wishlistView(model, wishlist);
	}

	private Wishlist findWishlistByMailId(String mailId) {
		return mailService.findWishlistById(mailId);
	}

	private String handleMailIdNotFound(ModelMap model) {
		model.addAttribute("message", invalidMailIdMessage);
		
		return ERROR_VIEW;
	}

	private String wishlistView(ModelMap model, Wishlist wishlist) {
		model.addAttribute("wishes", wishlist.getWishes());
		model.addAttribute("user", wishlist.getWisher());
		model.addAttribute("isWisher", false);
		model.addAttribute("wishlistIsEmpty", false);
		
		return WISHLIST_VIEW;
	}

}
