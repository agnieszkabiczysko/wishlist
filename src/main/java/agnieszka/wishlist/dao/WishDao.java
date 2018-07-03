package agnieszka.wishlist.dao;

import java.util.List;

import agnieszka.wishlist.model.Wish;

public interface WishDao {

	void save(Wish wish);
	
	void update(Wish wish);
	
	List<Wish> getAllWishes();
		
	Wish findWishById(int id);
}
