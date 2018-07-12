package agnieszka.wishlist.service;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;

@Service("wishlistService")
@Transactional
public class WishlistServiceImpl implements WishlistService {

	@Autowired
	private WishlistDao dao;
	
	@Autowired
	private UserPreferencesService preferencesService;
	
	@Override
	public void save(Wishlist wishlist) {
		dao.save(wishlist);
	}
	
	@Override
	public void update(Wishlist wishlist) {
		dao.update(wishlist);
	}

	@Override
	public boolean userWantsOffer(User user, Offer offer) {
		Set<Wishlist> wishlists = findWishlistsOf(user);
		return wishlists.stream().anyMatch(w -> w.contains(offer));
	}

	@Override
	public Wishlist getCurrentWishlistOf(User user) {
		return preferencesService.findUserPreferencesForUser(user).getCurrentWishlist();
	}

	@Override
	public void setCurrentWishlistOf(User user, Wishlist wishlist) {
		preferencesService.updateCurrentWishlist(user, wishlist);
	}

	@Override
	public Wishlist findWishlistById(int id) {
		return dao.findWishlistById(id);
	}

	@Override
	public Set<Wishlist> findWishlistsOf(User user) {
		return dao.getUserWishlists(user);
	}
	
	@Override
	public Set<Wishlist> findPublicWishlistsOf(User owner) {
		return findWishlistsOf(owner)
				.stream()
				.filter(Wishlist::isPublic)
				.collect(toSet());
	}

	@Override
	public Set<Wishlist> publicAndSharedWishlistsOf(User owner) {
		return findWishlistsOf(owner)
				.stream()
				.filter(wishlist -> wishlist.isPublic() || wishlist.isShared())
				.collect(toSet());
	}
}
