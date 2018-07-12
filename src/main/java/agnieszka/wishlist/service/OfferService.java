package agnieszka.wishlist.service;

import java.util.List;

import agnieszka.wishlist.model.Offer;

public interface OfferService {

	List<Offer> getOffers();

	Offer findOfferById(int id);

	void save(Offer offer);

	void update(Offer offer);

	List<Offer> findOfferByName(String searchFor);

	List<Offer> findOfferByVendor(String searchFor);
	
}
