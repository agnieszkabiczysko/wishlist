package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.controller.AddToWishlistController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class AddToWishlistControllerTest {

	@InjectMocks
	private AddToWishlistController controller;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private OfferService offerService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Mock
	private Principal principal;
	
	@Test
	public void addToWishlist() {
		//given
		Wishlist wishlist = wishlist("A");
		when(wishlistService.findWishlistById(1)).thenReturn(wishlist);
		when(offerService.findOfferById(1)).thenReturn(offer());
		
		mockUser(user());
		
		//when
		String view = controller.addOfferToWishlist(1, 1, principal);
		
		//then
		assertThat(view).isEqualTo("redirect:/myWishlists");
		assertThat(wishlist.contains(offer())).isEqualTo(true);
		assertThat(wishlist.getWishes()).hasSize(1);
		verify(wishlistService, times(1)).update(wishlist);
		verify(wishlistService, times(1)).setCurrentWishlistOf(user(), wishlist);
	}
	
	@Test
	public void addToCurrentWishlist() {
		//given
		Wishlist wishlist = wishlist("A");
		when(wishlistService.getCurrentWishlistOf(user())).thenReturn(wishlist);
		when(offerService.findOfferById(1)).thenReturn(offer());
		
		mockUser(user());
		
		//when
		String view = controller.addOfferToCurrentWishlist(1, principal);
		
		//then
		assertThat(view).isEqualTo("redirect:/myWishlists");
		assertThat(wishlist.contains(offer())).isEqualTo(true);
		assertThat(wishlist.getWishes()).hasSize(1);
		verify(wishlistService, times(1)).getCurrentWishlistOf(user());
		verify(wishlistService, times(1)).update(wishlist);
		verify(wishlistService, never()).findWishlistById(anyInt());
	}
	
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
	}
	
	private Wishlist wishlist(String name) {
		Wishlist wishlist = new Wishlist();
		wishlist.setName(name);
		return wishlist;
	}
	
	private Offer offer() {
		Offer offer = new Offer();
		offer.setName("X");
		return offer;
	}

	private User user() {
		User user = new User();
		user.setUserId("user");
		return user;
	}
	
}
