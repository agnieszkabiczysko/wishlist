package agnieszka.wishlist.dao;

import agnieszka.wishlist.model.WishlistMail;

public interface WishlistMailDao {

	void saveWishlistMail(WishlistMail wishlistMail);
	
	WishlistMail findWishlistMailById(String id);
	
}
