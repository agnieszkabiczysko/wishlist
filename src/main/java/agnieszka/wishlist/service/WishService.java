package agnieszka.wishlist.service;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;

public interface WishService {

	void save(Wish wish);
	
	void update(Wish wish);
	
	Wish findWishById(int id);

	void fulfilWish(Wish wish, User fulfiller);

}
