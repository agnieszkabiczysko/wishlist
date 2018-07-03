package agnieszka.wishlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.OfferDao;
import agnieszka.wishlist.model.Offer;

@Service("offerService")
@Transactional
public class OfferServiceImpl implements OfferService {

	
	@Autowired
	private OfferDao dao;
	
	@Override
	public List<Offer> getAllOffers() {
		return dao.getAllOffers();
	}

	@Override
	public Offer findOfferById(int id) {
		return dao.findOfferById(id);
	}

	@Override
	public void save(Offer offer) {
		dao.save(offer);
	}

	@Override
	public void update(Offer offer) {
		dao.update(offer);
	}

	@Override
	public List<Offer> findOfferByName(String searchFor) {
		return dao.findOfferByName(searchFor);
	}
	
	@Override
	public List<Offer> findOfferByVendor(String searchFor) {
		return dao.findOfferByVendor(searchFor);
	}

}
