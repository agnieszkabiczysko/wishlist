package agnieszka.wishlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishDao;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.WishState;

@Service("wishService")
@Transactional
public class WishServiceImpl implements WishService {

	@Autowired
	WishDao dao;
	
	@Override
	public void save(Wish wish) {
		dao.save(wish);
	}
	
	@Override
	public void update(Wish wish) {
		dao.update(wish);
	}

	@Override
	public List<Wish> getAllWishes() {
		return dao.getAllWishes();
	}

	@Override
	public Wish findWishById(int id) {
		return dao.findWishById(id);
	}

	@Override
	public void fulfilWish(Wish wish, User fulfiler) {
		wish.setFulfiller(fulfiler);
		wish.setState(WishState.PURCHASED);
		update(wish);
	}

	@Override
	public Boolean isWishPurchased(Wish wish) {
		return wish.getState().toString().equals("Purchased");
	}

}
