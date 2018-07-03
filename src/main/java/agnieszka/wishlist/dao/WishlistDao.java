package agnieszka.wishlist.dao;

import java.util.Set;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;

public interface WishlistDao {

	Wishlist findWishlistById(int id);

	Set<Wishlist> getUserWishlists(User wisher);

	void save(Wishlist wishlist);

	void update(Wishlist wishlist);

}
