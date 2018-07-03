package agnieszka.wishlist.dao;

import java.util.List;

import agnieszka.wishlist.model.Offer;

public interface OfferDao {

	void save(Offer offer);
	
	void update(Offer offer);
	
	List<Offer> getAllOffers();
	
	Offer findOfferById(int id);

	List<Offer> findOfferByName(String name);

	List<Offer> findOfferByVendor(String vendor);

}
