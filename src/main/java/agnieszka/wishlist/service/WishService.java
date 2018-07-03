package agnieszka.wishlist.service;

import java.util.List;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;

public interface WishService {

	void save(Wish wish);
	
	void update(Wish wish);
	
	List<Wish> getAllWishes();
		
	Wish findWishById(int id);

	void fulfilWish(Wish wish, User fulfiller);
	
	Boolean isWishPurchased(Wish wish);
}
