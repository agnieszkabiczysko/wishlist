package agnieszka.wishlist.service;

import static agnieszka.wishlist.service.helper.UUIDGenerator.generateUUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishlistMailDao;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;

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
	public Wishlist findWishlistById(String id) {
		WishlistMail wishlistMail = wishlistMailDao.findWishlistMailById(id);
		
		return (wishlistMail == null)
			? null
			: wishlistMail.getWishlist();
	}

	@Override
	public WishlistMail createWishlistMail(Wishlist wishlist) {
		WishlistMail wishlistMail = new WishlistMail(generateUUID(), wishlist);
		
		saveWishlistMail(wishlistMail);
		
		return wishlistMail;
	}
}
