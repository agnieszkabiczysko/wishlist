package agnieszka.wishlist.formatter;

import static java.util.stream.Collectors.joining;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.Wish;

@Component
public class WishFormatter {

	@Autowired
	OfferFormatter offerFormatter;
	
	public String formatWishlistForEmail(Set<Wish> wishlist) {
		return wishlist
				.stream()
				.map(this::formatWishForEmail)
				.collect(joining("\n"));
	}	

	private String formatWishForEmail(Wish wish) {
		return offerFormatter.formatOfferForEmail(wish.getOffer());
	}
	
}
