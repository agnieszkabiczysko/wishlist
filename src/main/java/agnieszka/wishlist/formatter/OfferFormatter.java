package agnieszka.wishlist.formatter;

import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.Offer;

@Component
public class OfferFormatter {

	public String formatOfferForEmail(Offer offer) {
		return offerTemplate(offer.getName(), offer.getVendor(), offer.getDescription());
	}
	
	private String offerTemplate(String name, String vendor, String descritpion) {
		return String.format("Moje życzenie: %s %nProducent: %s %nOpis: %s",
				name, vendor, descritpion);
	}
}
