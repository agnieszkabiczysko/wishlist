package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.ShowWishlistController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class ShowWishlistControllerTest {

	@InjectMocks
	private ShowWishlistController controller;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@SuppressWarnings("unchecked")
	@Test
	public void showEmptyWishlist() {
		//given
		Wishlist emptyWishlist = new Wishlist();
		emptyWishlist.setWisher(user());
		when(wishlistService.findWishlistById(1)).thenReturn(emptyWishlist);
		
		mockUser(user());
		
		//when
		String view = controller.showWishlist(1, null, model, principal);
		
		//then
		assertThat(view).isEqualTo("wishes");
		assertThat(model.get("wishlistIsEmpty")).isEqualTo(true);
		assertThat((List<Wish>) model.get("wishes")).hasSize(0);
		assertThat(model.get("wishlistId")).isEqualTo(1);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void wishesAreSorted() {
		//given
		Wishlist wishlist = wishlist(offer("B"), offer("A"), offer("D"), offer("C"));
		wishlist.setWisher(user());
		
		when(wishlistService.findWishlistById(2)).thenReturn(wishlist);
		
		mockUser(user());
		
		//when
		controller.showWishlist(2, null, model, principal);
		
		//then
		assertThat((List<Wish>) model.get("wishes")).isSortedAccordingTo((w1, w2) -> w1.getOffer().getName().compareTo(w2.getOffer().getName()));
	}
	
	@Test
	public void showNonEmptyWishlistForOwner() {
		//given
		Wishlist wishlist = wishlist(offer("A"), offer("B"));
		wishlist.setWisher(user());
		
		when(wishlistService.findWishlistById(3)).thenReturn(wishlist);
		
		mockUser(user());
		
		//when
		String view = controller.showWishlist(3, null, model, principal);
		
		//then
		assertThat(view).isEqualTo("wishes");
		assertThat(model.get("wishlistIsEmpty")).isEqualTo(false);
		assertThat(model.get("user")).isEqualTo(user());
		assertThat(model.get("isWisher")).isEqualTo(true);
		assertThat(model.get("wishlistId")).isEqualTo(3);
	}
	
	@Test
	public void showNonEmptyWishlistForNonOwner() {
		//given
		Wishlist wishlist = wishlist(offer("A"), offer("B"));
		wishlist.setWisher(user());
		
		when(wishlistService.findWishlistById(4)).thenReturn(wishlist);
		
		mockUser(anotherUser());
		
		//when
		String view = controller.showWishlist(4, null, model, principal);
		
		//then
		assertThat(view).isEqualTo("wishes");
		assertThat(model.get("wishlistIsEmpty")).isEqualTo(false);
		assertThat(model.get("user")).isEqualTo(user());
		assertThat(model.get("isWisher")).isEqualTo(false);
		assertThat(model.get("wishlistId")).isEqualTo(4);
	}
	
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
	}
	
	private User user() {
		User user = new User();
		user.setUserId("testuser");
		return user;
	}
	
	private User anotherUser() {
		User user = new User();
		user.setUserId("anotheruser");
		return user;
	}
	
	private Wishlist wishlist(Offer... offers) {
		Wishlist wishlist = new Wishlist();
		
		Arrays.asList(offers).stream().forEach(wishlist::add);
		
		return wishlist;
	}
	
	private Offer offer(String name) {
		Offer offer = new Offer();
		offer.setName(name);
		return offer;
	}
}
