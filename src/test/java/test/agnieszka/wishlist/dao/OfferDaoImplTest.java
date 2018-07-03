package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.OfferDao;
import agnieszka.wishlist.model.Offer;

public class OfferDaoImplTest extends EntityDaoImplTest {

	@Autowired
	private OfferDao dao;
	
	private Offer offer;
	private Offer anotherOffer;
	private List<Offer> offers;
	
	@Before
	public void setup() {
		offer = getSampleOffer();
		anotherOffer = getSecondOffer();
		offers = new ArrayList<>();
		offers.add(offer);
		offers.add(anotherOffer);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveOfferInDb() {
		//when
		dao.save(offer);
		
		//then
		assertThat(dao.getAllOffers().size()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllOfferInDb() {
		populateDbWithOffers();
		
		//when
		List<Offer> offersFromDb = dao.getAllOffers();
		
		//then
		assertEquals(offersFromDb, offers);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOfferByIdInDb() {
		//given
		dao.save(offer);
		
		//when
		Offer offerFromDb = dao.findOfferById(offer.getId());
		
		//then
		assertThat(offerFromDb).isEqualTo(offer);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOfferByName() {
		populateDbWithOffers();
		
		//when
		List<Offer> offersFromDb = dao.findOfferByName("OfertaPróbna");
		
		//then
		assertThat(offersFromDb).containsExactly(offer);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOfferByVendorIfItsNameIsExaclyTheSame() {
		populateDbWithOffers();
		
		//when
		List<Offer> offersFromDb = dao.findOfferByVendor("ProducentPierwszy");
		
		//then
		assertThat(offersFromDb).containsExactly(offer);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOfferByVendorIfWordPassToDiffrentOffer() {
		//given
		populateDbWithOffers();
		
		//when
		List<Offer> offersFromDb = dao.findOfferByVendor("Producent");
		
		//then
		assertEquals(offersFromDb, offers);
	}

	private void populateDbWithOffers() {
		dao.save(offer);
		dao.save(anotherOffer);
	}
	
	private Offer getSampleOffer() {
		Offer offer = new Offer();
		offer.setName("OfertaPróbna");
		offer.setVendor("ProducentPierwszy");
		return offer;
	}
	
	private Offer getSecondOffer() {
		Offer offer = new Offer();
		offer.setName("OfertaDruga");
		offer.setVendor("ProducentJeszczeJeden");
		return offer;
	}
}
