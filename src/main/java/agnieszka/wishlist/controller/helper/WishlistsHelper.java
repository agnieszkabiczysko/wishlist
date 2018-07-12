package agnieszka.wishlist.controller.helper;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@Component
public class WishlistsHelper {

	@Autowired
	private WishlistService wishlistService;

	public Set<Wishlist> wishlistsOfVisibleFor(User owner, User visibleFor) {
		Set<Wishlist> wishlists = (visibleFor == null)
				? publicWishlistsOf(owner)
				: wishlistsOfVisibleForLoggedInUser(owner, visibleFor);
				
		return nonEmptyWishlists(wishlists);
	}
	
	public Set<Wishlist> wishlistsOf(User owner) {
		return allWishlistsOf(owner);
	}

	private Set<Wishlist> wishlistsOfVisibleForLoggedInUser(User owner, User currentUser) {
		if (currentUser.equals(owner)) {
			return allWishlistsOf(owner);
		}
		
		if (currentUser.isFriendOf(owner)) {
			return publicAndSharedWishlistsOf(owner);
		}
		
		return publicWishlistsOf(owner);
	}

	private Set<Wishlist> publicWishlistsOf(User user) {
		return wishlistService.findPublicWishlistsOf(user);
	}

	private Set<Wishlist> allWishlistsOf(User user) {
		return wishlistService.findWishlistsOf(user);
	}
			
	private Set<Wishlist> publicAndSharedWishlistsOf(User owner) {
		return wishlistService.publicAndSharedWishlistsOf(owner);
	}

	private Set<Wishlist> nonEmptyWishlists(Set<Wishlist> wishlists) {
		return wishlists
				.stream()
				.filter(Wishlist::hasWishes)
				.collect(toSet());
	}

}
