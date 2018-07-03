package agnieszka.wishlist.service;

import java.util.Set;

import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.model.WishlistTermType;

public interface WishlistService {

	Wishlist findWishlistById(int id);

	Set<Wishlist> getAllWishlistsForUser(User user);
	
	boolean userWantsOffer(User user, Offer offer);

	void save(Wishlist wishlist);
	
	void update(Wishlist wishlist);

	Set<Wishlist> findWishlistsByTerm(WishlistTermType type, String term);
	
	Set<Wishlist> findWishlistsByTermAndState(WishlistTermType type, String term, WishlistState state);

	Set<Wishlist> sharedAndPublicWishlistsForUser(User wisher, User visibleFor);

}
