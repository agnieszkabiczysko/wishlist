package agnieszka.wishlist.formatter;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.Wish;

@Component
public class WishFormatter {

	@Autowired
	OfferFormatter offerFormatter;
	
	public String formatWishForEmail(Wish wish) {
		return offerFormatter.formatOfferForEmail(wish.getOffer());
	}
	
	public String formatWishlistForEmail(Set<Wish> wishlist) {
		return wishlist.stream().map(this::formatWishForEmail).collect(Collectors.joining("\n"));
	}	
}
