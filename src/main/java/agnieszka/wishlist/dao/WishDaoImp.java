package agnieszka.wishlist.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.Wish;

@Repository("wishDao")
public class WishDaoImp extends AbstractDao<Integer, Wish> implements WishDao {

	@Override
	public void save(Wish wish) {
		persist(wish);
	}

	@Override
	public void update(Wish wish) {
		merge(wish);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Wish> getAllWishes() {
		Criteria criteria = createEntityCriteria();
		return (List<Wish>) criteria.list();
	}

	@Override
	public Wish findWishById(int id) {
		return getByKey(id);
	}

}
