package test.agnieszka.wishlist.unit.service;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.dao.OfferDao;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.OfferServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceImplTest {

	@Mock
	private OfferDao dao;
	
	@InjectMocks
	private OfferServiceImpl offerService;
	
	@Test
	public void findAllOffer() {
		//given
		List<Offer> offers = offers();
		when(dao.getAllOffers()).thenReturn(offers);
		
		//when
		List<Offer> offersFound = offerService.getOffers();
		
		//then
		verify(dao, times(1)).getAllOffers();
		assertEquals(offers, offersFound);
	}
	
	@Test
	public void findOfferById() {
		//given
		Offer offer = anOffer();
		int offerId = offer.getId();
		
		when(dao.findOfferById(offerId)).thenReturn(offer);
		
		//when
		Offer offerFound = offerService.findOfferById(offerId);
		
		//then
		verify(dao, times(1)).findOfferById(offerId);
		assertEquals(offer, offerFound);
	}
	
	@Test
	public void saveNewOffer() {
		//given
		Offer offer = anOffer();
		doNothing().when(dao).save(offer);

		//when
		offerService.save(offer);
		
		//then
		verify(dao, times(1)).save(offer);
	}
	
	@Test
	public void updateOffer() {
		//given
		Offer offer = anOffer();
		doNothing().when(dao).update(offer);
		
		//when
		offerService.update(offer);
		
		//then
		verify(dao, times(1)).update(offer);
	}
	
	@Test
	public void findOffersByName() {
		//given
		List<Offer> offers = offers();
		String offerName = "test";
		
		when(dao.findOfferByName(offerName)).thenReturn(offers);

		//when
		List<Offer> offersFound = offerService.findOfferByName(offerName);
		
		//then
		verify(dao, times(1)).findOfferByName(offerName);
		assertEquals(offers, offersFound);
	}
	
	@Test
	public void findOffersByVendor() {
		//given
		List<Offer> offers = offers();
		String vendorName = "testVendor";
		
		when(dao.findOfferByVendor(vendorName)).thenReturn(offers);

		//when
		List<Offer> offersFound= offerService.findOfferByVendor(vendorName);
		
		//then
		verify(dao, times(1)).findOfferByVendor(vendorName);
		assertEquals(offers, offersFound);
	}
	
	private User aUser() {
		User u1 = new User();
		u1.setFirstname("User1");
		u1.setLastname("Testowy");
		u1.setUserId("Testowy1");
		
		return u1;
	}
	
	private User anotherUser() {
		User u2 = new User();
		u2.setFirstname("User2");
		u2.setLastname("Testowy");
		u2.setUserId("Testowy2");

		return u2;
	}
	
	private Offer anOffer() {
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		o1.setVendor("Testowy");
		o1.setDescription("Testujemy");
		o1.setOfferSeller(aUser());
		
		return o1;
	}
	
	private Offer anotherOffer() {
		Offer o2 = new Offer();
		o2.setName("Oferta2");
		o2.setVendor("Testowy");
		o2.setDescription("Testujemy");
		o2.setOfferSeller(anotherUser());
		
		return o2;
	}
	
	private List<Offer> offers(Offer... offers) {
		return asList(offers);
	}
	
	private List<Offer> offers() {
		return offers(anOffer(), anotherOffer());
	}

}
