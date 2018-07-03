package agnieszka.wishlist.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.converter.EmailFromString;
import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.model.WishlistTermType;

@Service("wishlistService")
@Transactional
public class WishlistServiceImpl implements WishlistService {

	@Autowired
	private WishlistDao dao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailFromString emailFromString;
	
	@Override
	public Wishlist findWishlistById(int id) {
		return dao.findWishlistById(id);
	}

	@Override
	public Set<Wishlist> getAllWishlistsForUser(User user) {
		return dao.getUserWishlists(user);
	}

	@Override
	public boolean userWantsOffer(User user, Offer offer) {
		Set<Wishlist> wishlists = getAllWishlistsForUser(user);
		return wishlists.stream().anyMatch(w -> w.contains(offer));
	}

	@Override
	public void save(Wishlist wishlist) {
		dao.save(wishlist);
	}
	
	@Override
	public void update(Wishlist wishlist) {
		dao.update(wishlist);
	}

	@Override
	public Set<Wishlist> findWishlistsByTerm(WishlistTermType type, String term) {
		Set<Wishlist> foundedWishlists = new HashSet<>();
		if (type == WishlistTermType.EMAIL) {
			foundedWishlists = findWishlistsByEmail(term);
		} else if (type == WishlistTermType.USERID) {
			foundedWishlists = findWishlistsByUser(term);
		}
		return foundedWishlists;
	}
	

	@Override
	public Set<Wishlist> findWishlistsByTermAndState(WishlistTermType type, String term, WishlistState state) {
		Set<Wishlist> foundWishlistsByTerm = new HashSet<>();
		Set<Wishlist> foundWishlistsByTermAndState = new HashSet<>();
		for (Wishlist wishlist : findWishlistsByTerm(type, term)) {
			foundWishlistsByTerm.add(wishlist);
		}
		for (Wishlist wishlist : foundWishlistsByTerm) {
			if (wishlist.getState() == state) {
				foundWishlistsByTermAndState.add(wishlist);
			}
		}
		return foundWishlistsByTermAndState;
	}

	@Override
	public Set<Wishlist> sharedAndPublicWishlistsForUser(User wisher, User visibleFor) {
		Set<Wishlist> sharedAndPublicWishlists = new HashSet<>();
		for (Wishlist wishlist : findWishlistsByUser(wisher.getUserId())) {
			if (isWishlistPublic(wishlist) || (isWishlistShared(wishlist) && isCurrentUserFriendOfWisher(wisher, visibleFor))) {
				sharedAndPublicWishlists.add(wishlist);
			};
		}
		return sharedAndPublicWishlists;
	}

	private Set<Wishlist> findWishlistsByUser(String userId) {
		return dao.getUserWishlists(userService.findUserByUserId(userId));
	}

	private Set<Wishlist> findWishlistsByEmail(String email) {
		EmailAddress emailAddress = emailFromString.convert(email);
		return dao.getUserWishlists(userService.findUserByEmail(emailAddress));
	}

	private boolean isCurrentUserFriendOfWisher(User wisher, User currentUser) {
		for (User user: wisher.getFriends()) {
			if (currentUser.equals(user)) {
				return true;
			}
		}
		return false;
	}

	private boolean isWishlistPublic(Wishlist wishlist) {
		return wishlist.getState().equals(WishlistState.PUBLIC);
	}
	
	private boolean isWishlistShared(Wishlist wishlist) {
		return wishlist.getState().equals(WishlistState.SHARED);
	}
}
