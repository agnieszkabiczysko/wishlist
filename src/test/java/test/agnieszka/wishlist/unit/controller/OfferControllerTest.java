package test.agnieszka.wishlist.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.controller.OfferController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.SearchType;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.WishlistService;

public class OfferControllerTest {

	@Mock
	private OfferService offerService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private UserPreferencesService preferencesService;
	
	@InjectMocks
	private OfferController controller;
	
	@Spy
	private List<Offer> offers = new ArrayList<>();
	
	private Offer offer;
	private Wishlist wishlist = new Wishlist();
	private Set<Wishlist> wishlists = new HashSet<>();
	private User user;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private BindingResult result;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		user = user();
		offer = offer(user);
		offers = getOffersList();
		wishlist = getWishlist();
		wishlists.add(wishlist);
	}

	@Test
	public void showAllOffers() {
		//given
		when(offerService.getAllOffers()).thenReturn(offers);
		
		//when
		String viewName = controller.showAllOffers(model);
		
		//then
		assertEquals("offers", viewName);
		assertEquals(offers, model.get("offers"));
		verify(offerService,times(1)).getAllOffers();
	}
	
	@Test
	public void showChoicedOfferForUnknownUser() {
		//given
		when(offerService.findOfferById(1)).thenReturn(offer);
		
		//when
		String viewName = controller.showOffer(model, offer.getId(), null);
		
		//then
		assertEquals("offer", viewName);
		assertEquals(offer, model.get("offer"));
		verify(offerService,times(1)).findOfferById(anyInt());
	}
	
	@Test
	public void showChoicedOfferForUserWithoutUserPreferences() {
		//given
		injectUserIntoMockServices(user);
		when(preferencesService.userHasCurrentWishlist(user)).thenReturn(false);
		when(offerService.findOfferById(1)).thenReturn(offer);
		
		//when
		String viewName = controller.showOffer(model, offer.getId(), principal);
		
		//then
		assertEquals("offer", viewName);
		assertEquals(offer, model.get("offer"));
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(false, model.get("userHasCurrentWishlist"));
		verify(offerService,times(1)).findOfferById(anyInt());
	}
	
	@Test
	public void showChoicedOfferForUserWithUserPreferences() {
		//given
		injectUserIntoMockServices(user);
		when(preferencesService.userHasCurrentWishlist(user)).thenReturn(true);
		when(offerService.findOfferById(1)).thenReturn(offer);
		
		//when
		String viewName = controller.showOffer(model, offer.getId(), principal);
		
		//then
		assertEquals("offer", viewName);
		assertEquals(offer, model.get("offer"));
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(true, model.get("userHasCurrentWishlist"));
		verify(offerService,times(1)).findOfferById(anyInt());
	}
	
	@Test
	public void showChoicedOfferForOfferSeller() {
		//given
		injectUserIntoMockServices(user);
		when(preferencesService.userHasCurrentWishlist(user)).thenReturn(false);
		when(offerService.findOfferById(1)).thenReturn(offer);
		
		//when
		String viewName = controller.showOffer(model, offer.getId(), principal);
		
		//then
		assertEquals("offer", viewName);
		assertEquals(true, model.get("canManage"));
		verify(offerService,times(1)).findOfferById(anyInt());
	}

	
	@Test
	public void showAddNewOfferForm() {
		//when
		Offer newOffer = new Offer();
		String viewName = controller.showAddNewOfferForm(model, newOffer);
		
		//then
		assertEquals("addNewOffer", viewName);
		verify(model, times(1)).addAttribute(eq("offer"), any(Offer.class));
	}
	
	
	@Test
	public void processAddNewOfferFormWithFailure() {
		//given
		Offer newOffer = new Offer();
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.processAddNewOfferForm(newOffer, result, request, principal);
		
		//then
		assertEquals("addNewOffer", viewName);
	}

	
	@Test
	public void processAddNewOfferFormWithSuccess() {
		//given
		when(result.hasErrors()).thenReturn(false);
		injectUserIntoMockServices(user);
		doNothing().when(offerService).save(offer);
		
		//when
		String viewName = controller.processAddNewOfferForm(offer, result, request, principal);
		
		//then
		assertEquals("redirect:/offers", viewName);
		verify(offerService, times(1)).save(offer);
	}
	
	
	@Test
	public void editOfferByOfferSeler() {
		//given
		when(offerService.findOfferById(1)).thenReturn(offer);
		injectUserIntoMockServices(user);
		
		//when
		String viewName = controller.editOffer(offer.getId(), model, principal);
		
		//then
		assertEquals("addNewOffer", viewName);
		assertEquals(offer, model.get("offer"));
		assertNotNull(model.get("offer"));
		assertTrue((Boolean) model.get("edit"));
	}
	
	
	@Test
	public void updateOfferWithoutSuccess() {
		//given
		when(result.hasErrors()).thenReturn(true);
		
		//when
		String viewName = controller.updateOffer(offer.getId(), model, offer, result, request);
		
		//then
		assertEquals("addNewOffer", viewName);
	}
	
	
	@Test
	public void updateOfferWithSuccess() {
		//given
		when(result.hasErrors()).thenReturn(false);
		doNothing().when(offerService).update(offer);
		
		//when
		String viewName = controller.updateOffer(offer.getId(), model, offer, result, request);
		
		//then
		assertEquals("redirect:/offers", viewName);
		verify(offerService, times(1)).update(any(Offer.class));
	}
	
	
	@Test
	public void showSearchOfferForm() {
		//when
		String viewName = controller.showSearchOfferForm();
		
		//then
		assertEquals("findOffer", viewName);
		
	}
	
	@Test
	public void showSearchOfferResultsBasedOnName() {
		//given
		String searchType = SearchType.NAME.toString();
		when(offerService.findOfferByName(anyString())).thenReturn(offers);
		
		//when
		String viewName = controller.showSearchOfferResults(model, anyString(), searchType);
		
		//then
		assertEquals("offers", viewName);
		assertEquals(offers, model.get("offers"));
	}
	
	@Test
	public void showSearchOfferResultsBasedOnVendor() {
		//given
		String searchType = SearchType.VENDOR.toString();
		when(offerService.findOfferByVendor(anyString())).thenReturn(offers);
		
		//when
		String viewName = controller.showSearchOfferResults(model, anyString(), searchType);
		
		//then
		assertEquals("offers", viewName);
		assertEquals(offers, model.get("offers"));
	}
	
	
	private void injectUserIntoMockServices(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
		when(wishlistService.getAllWishlistsForUser(user)).thenReturn(wishlists);
	}
	
	private List<Offer> getOffersList() {
		Offer o1 = offer(user());
		Offer o2 = anotherOffer(anotherUser());
		offers.add(o1);
		offers.add(o2);
		return offers;
	}

	private User user() {
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

	private Offer offer(User user) {
		Offer o1 = new Offer();
		o1.setId(1);
		o1.setName("Oferta1");
		o1.setVendor("Testowy");
		o1.setDescription("Testujemy");
		o1.setOfferSeller(user);
		return o1;
	}

	private Offer anotherOffer(User user) {
		Offer o2 = new Offer();
		o2.setId(2);
		o2.setName("Oferta2");
		o2.setVendor("Testowy");
		o2.setDescription("Testujemy");
		o2.setOfferSeller(user);
		return o2;
	}
	
	private Wishlist getWishlist() {
		wishlist.add(new Wish(offers.get(0)));
		return wishlist;
	}
}
