package agnieszka.wishlist.service;

import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;

public interface WishlistMailService {

	void saveWishlistMail(WishlistMail wishlistMail);
	
	Wishlist findWishlistById(String id);
	
	WishlistMail createWishlistMail (Wishlist wishlist);
}
