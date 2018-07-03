package agnieszka.wishlist.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;

@Repository("wishlistDao")
public class WishlistDaoImpl extends AbstractDao<Integer, Wishlist> implements WishlistDao {

	@Override
	public Wishlist findWishlistById(int id) {
		return getByKey(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Wishlist> getUserWishlists(User wisher) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("wisher", wisher));
		filterDuplicates(criteria);
		return new HashSet<>((List<Wishlist>) criteria.list());
	}

	private void filterDuplicates(Criteria criteria) {
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}

	@Override
	public void save(Wishlist wishlist) {
		persist(wishlist);
	}

	@Override
	public void update(Wishlist wishlist) {
		merge(wishlist);
	}

}
