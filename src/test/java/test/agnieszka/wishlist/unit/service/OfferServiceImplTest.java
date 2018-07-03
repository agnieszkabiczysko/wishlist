package test.agnieszka.wishlist.unit.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import agnieszka.wishlist.dao.OfferDao;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.OfferServiceImpl;

public class OfferServiceImplTest {

	@Spy
	private List<Offer> offers = new ArrayList<>();
	
	@Mock
	private OfferDao dao;
	
	@InjectMocks
	private OfferServiceImpl offerService;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		offers = getOffersList();
	}
	
	@Test
	public void findAllOffer() {
		//given
		when(dao.getAllOffers()).thenReturn(offers);
		
		//when
		List<Offer> foundedOffers = offerService.getAllOffers();
		
		//then
		verify(dao, times(1)).getAllOffers();
		assertEquals(offers, foundedOffers);
	}
	
	@Test
	public void findOfferById() {
		//given
		Offer offer = offers.get(0);
		when(dao.findOfferById(anyInt())).thenReturn(offer);
		
		//when
		Offer foundedOffer = offerService.findOfferById(anyInt());
		
		//then
		verify(dao, times(1)).findOfferById(anyInt());
		assertEquals(offers.get(0), foundedOffer);
	}
	
	@Test
	public void saveNewOfferInDb() {
		//given
		Offer offer = offers.get(0);
		doNothing().when(dao).save(any(Offer.class));

		//when
		offerService.save(offer);
		
		//then
		verify(dao, times(1)).save(any(Offer.class));
	}
	
	@Test
	public void updateOffer() {
		//given
		Offer offer = offers.get(0);
		doNothing().when(dao).update(any(Offer.class));
		
		//when
		offerService.update(offer);
		
		//then
		verify(dao, times(1)).update(any(Offer.class));
	}
	
	@Test
	public void findOffersByName() {
		//given
		when(dao.findOfferByName(anyString())).thenReturn(offers);

		//when
		List<Offer> foundedOffer = offerService.findOfferByName(anyString());
		
		//then
		verify(dao, times(1)).findOfferByName(anyString());
		assertEquals(offers, foundedOffer);
	}
	
	@Test
	public void findOffersByVendor() {
		//given
		when(dao.findOfferByVendor(anyString())).thenReturn(offers);

		//when
		List<Offer> foundedOffer= offerService.findOfferByVendor(anyString());
		
		//then
		verify(dao, times(1)).findOfferByVendor(anyString());
		assertEquals(offers, foundedOffer);
	}
	
	
	private List<Offer> getOffersList() {
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		o1.setVendor("Testowy");
		o1.setDescription("Testujemy");
		
		User u1 = new User();
		u1.setFirstname("User1");
		u1.setLastname("Testowy");
		u1.setUserId("Testowy1");
		
		o1.setOfferSeller(u1);
		
		Offer o2 = new Offer();
		o2.setName("Oferta2");
		o2.setVendor("Testowy");
		o2.setDescription("Testujemy");
		
		User u2 = new User();
		u2.setFirstname("User2");
		u2.setLastname("Testowy");
		u2.setUserId("Testowy2");
		
		o2.setOfferSeller(u2);
		
		offers.add(o1);
		offers.add(o2);
		
		return offers;
	}

}
