package agnieszka.wishlist.dao;

import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.WishlistMail;

@Repository("wishlistMailDao")
public class WishlistMailDaoImpl extends AbstractDao<String, WishlistMail> implements WishlistMailDao {

	@Override
	public void saveWishlistMail(WishlistMail wishlistMail) {
		persist(wishlistMail);
	}

	@Override
	public WishlistMail findWishlistMailById(String id) {
		return getByKey(id);
	}

}
