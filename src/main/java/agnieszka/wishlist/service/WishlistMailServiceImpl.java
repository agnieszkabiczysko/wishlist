package agnieszka.wishlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishlistMailDao;
import agnieszka.wishlist.exception.InvalidWishlistMailIdException;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.service.helper.GeneratorUUID;

@Service("wishlistMailService")
@Transactional
public class WishlistMailServiceImpl implements WishlistMailService {

	@Autowired
	private WishlistMailDao wishlistMailDao;
	
	@Override
	public void saveWishlistMail(WishlistMail wishlistMail) {
		wishlistMailDao.saveWishlistMail(wishlistMail);
	}

	@Override
	public Wishlist findWishlistById(String id) throws InvalidWishlistMailIdException {
		WishlistMail wishlistMail = wishlistMailDao.findWishlistMailById(id);
		if (wishlistMail == null) {
			throw new InvalidWishlistMailIdException();
		}
		return wishlistMail.getWishlist();
	}

	@Override
	public WishlistMail createWishlistMail(Wishlist wishlist) {
		WishlistMail wishlistMail = new WishlistMail(GeneratorUUID.generateUUID(), wishlist);
		wishlistMailDao.saveWishlistMail(wishlistMail);
		return wishlistMail;
	}
}
