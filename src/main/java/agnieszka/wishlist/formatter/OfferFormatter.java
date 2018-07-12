package agnieszka.wishlist.formatter;

import static java.text.MessageFormat.format;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.Offer;

@Component
public class OfferFormatter {
	@Value("${offerFormatter.template}")
	private String offerTemplate;

	public String formatOfferForEmail(Offer offer) {
		return format(offerTemplate, offer.getName(), offer.getVendor(), offer.getDescription());
	}
}
