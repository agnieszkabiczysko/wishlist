package test.agnieszka.wishlist.unit.controller;

import static agnieszka.wishlist.model.WishlistState.PRIVATE;
import static agnieszka.wishlist.model.WishlistState.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.ShowOfferController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class ShowOfferControllerTest {
	
	@InjectMocks
	private ShowOfferController controller;

	@Mock
	private OfferService offerService;
	
	@Mock
	private UserPreferencesService userPreferencesService;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	private List<Offer> offers = new ArrayList<>();
	
	private Offer anOffer;
	private Offer anotherOffer;
	
	private User aUser;
	
	@Before
	public void setup() {
		aUser = aUser();
		
		anOffer = anOffer();
		anotherOffer = anotherOffer();
		
		offers.add(anOffer);
		offers.add(anotherOffer);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void showAllOffersTest() {
		//given
		when(offerService.getOffers()).thenReturn(offers);
		
		//when
		String viewName = controller.showOffers(model);
		
		//then
		assertEquals("offers", viewName);
		assertEquals(offers, model.get("offers"));
		assertThat((List<Offer>) model.get("offers")).containsExactly(anotherOffer, anOffer);
		verify(offerService, times(1)).getOffers();
	}

	@Test
	public void showSingleOfferNonLoggedTest() {
		//given
		when(offerService.findOfferById(1)).thenReturn(anotherOffer);
		
		//when
		String viewName = controller.showOffer(model, 1, principal);
		
		//then
		assertEquals("offer", viewName);
	}

	@Test
	public void showSingleOfferToLoggedInOwnerTest() {
		//given
		Set<Wishlist> wishlists = new HashSet<>();
		wishlists.add(new Wishlist("A", PUBLIC));
		wishlists.add(new Wishlist("B", PRIVATE));
		
		when(offerService.findOfferById(1)).thenReturn(anotherOffer);
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(aUser);
		when(userPreferencesService.userHasCurrentWishlist(aUser)).thenReturn(true);
		when(wishlistService.findWishlistsOf(aUser)).thenReturn(wishlists);
		
		//when
		String viewName = controller.showOffer(model, 1, principal);
		
		//then
		assertEquals("offer", viewName);
		assertEquals(true, model.get("canManage"));
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(true, model.get("userHasCurrentWishlist"));
	}

	private User aUser() {
		User user = new User();
		return user;
	}

	private Offer anOffer() {
		Offer offer = new Offer();
		offer.setName("R");
		return offer;
	}

	private Offer anotherOffer() {
		Offer offer = new Offer();
		offer.setName("L");
		offer.setOfferSeller(aUser);
		return offer;
	}

}
