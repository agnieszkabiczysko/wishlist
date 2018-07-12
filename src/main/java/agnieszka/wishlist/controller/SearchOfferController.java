package agnieszka.wishlist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.SearchType;
import agnieszka.wishlist.service.OfferService;

@Controller
public class SearchOfferController {

	private static final String FIND_OFFERS_FORM_VIEW = "findOffer";
	private static final String FIND_OFFERS_RESULTS_VIEW = "offers";
	
	@Autowired
	private OfferService offerService;
	
	@RequestMapping(value = "/searchOffer")
	public String showSearchOfferForm() {
		return FIND_OFFERS_FORM_VIEW;
	}
	
	@RequestMapping(value = "/searchOffer", params = {"searchFor", "searchTypeString"})
	public String showSearchOfferResults(ModelMap model, @RequestParam String searchFor, @RequestParam String searchTypeString) {
		
		List<Offer> offers = findOffers(searchFor, searchType(searchTypeString));
		
		model.addAttribute("offers", offers);
		
		return FIND_OFFERS_RESULTS_VIEW;
	
	}

	private SearchType searchType(String searchTypeAsString) {
		return SearchType.valueOf(searchTypeAsString.toUpperCase());
	}
	
	private List<Offer> findOffers(String searchFor, SearchType searchType) {
		switch (searchType) {
		case NAME:
			return offerService.findOfferByName(searchFor);
		case VENDOR:
			return offerService.findOfferByVendor(searchFor);
		default:
			throw new IllegalArgumentException("Unknown search type: " + searchType);
		}
	}
}
