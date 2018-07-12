package agnieszka.wishlist.service;

import java.util.Set;

import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;

public interface WishlistService {

	void save(Wishlist wishlist);
	
	void update(Wishlist wishlist);

	boolean userWantsOffer(User user, Offer offer);

	Wishlist findWishlistById(int id);

	Set<Wishlist> findWishlistsOf(User user);
	
	Set<Wishlist> findPublicWishlistsOf(User owner);

	Set<Wishlist> publicAndSharedWishlistsOf(User owner);

	Wishlist getCurrentWishlistOf(User user);

	void setCurrentWishlistOf(User user, Wishlist wishlist);

}
