package agnieszka.wishlist.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.Offer;

@Repository("offerDao")
public class OfferDaoImpl extends AbstractDao<Integer, Offer> implements OfferDao {

	@Override
	public void save(Offer offer) {
		persist(offer);
	}
	
	@Override
	public void update(Offer offer) {
		merge(offer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Offer> getAllOffers() {
		Criteria criteria = createEntityCriteria();
		return (List<Offer>) criteria.list();
	}

	@Override
	public Offer findOfferById(int id) {
		return getByKey(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Offer> findOfferByName(String value) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.ilike("name", value, MatchMode.ANYWHERE));
		return (List<Offer>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Offer> findOfferByVendor(String value) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.ilike("vendor", value, MatchMode.ANYWHERE));
		return (List<Offer>) criteria.list();
	}
}
