package test.agnieszka.wishlist.unit.controller.helper;

import static agnieszka.wishlist.model.WishlistState.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.controller.helper.WishlistsHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class WishlistsHelperTest {

	@InjectMocks
	private WishlistsHelper wishlistsHelper;
	
	@Mock
	private WishlistService wishlistService;
	
	@Test
	public void nonLoggedUsersShouldSeeOnlyPublicWishlists() {
		//given
		User owner = user();
		
		//when
		wishlistsHelper.wishlistsOfVisibleFor(owner, null);
		
		//then
		verify(wishlistService, times(1)).findPublicWishlistsOf(owner);
	}
	
	@Test
	public void ownerShouldSeeAllWishlists() {
		//given
		User owner = user();
		User currentUser = owner;
		
		//when
		wishlistsHelper.wishlistsOfVisibleFor(owner, currentUser);
		
		//then
		verify(wishlistService, times(1)).findWishlistsOf(owner);
	}
	
	@Test
	public void friendShouldSeePublicAndSharedWishlists() {
		//given
		User owner = user();
		User currentUser = anotherUser();
		
		owner.addFriend(currentUser);
		
		//when
		wishlistsHelper.wishlistsOfVisibleFor(owner, currentUser);
		
		//then
		verify(wishlistService, times(1)).publicAndSharedWishlistsOf(owner);
	}
	
	@Test
	public void anotherUserShouldSeeOnlyPublicWishlists() {
		//given
		User owner = user();
		User currentUser = anotherUser();
		
		//when
		wishlistsHelper.wishlistsOfVisibleFor(owner, currentUser);
		
		//then
		verify(wishlistService, times(1)).findPublicWishlistsOf(owner);
	}
	
	@Test
	public void emptyWishlistsAreFilteredOut() {
		//given
		User owner = user();
		
		when(wishlistService.findPublicWishlistsOf(owner)).thenReturn(wishlists());
		
		//when
		Set<Wishlist> wishlists = wishlistsHelper.wishlistsOfVisibleFor(owner, null);
		
		//then
		assertThat(wishlists).containsOnly(publicWishlist());
	}
	
	@Test
	public void wishlistsSearchShouldReturnAllNonEmptyUsersWishlists() {
		//given
		User owner = user();
		
		when(wishlistService.findWishlistsOf(owner)).thenReturn(wishlists());

		//when
		Set<Wishlist> wishlists = wishlistsHelper.wishlistsOfVisibleFor(owner, owner);
		
		//then
		assertThat(wishlists).containsOnly(publicWishlist());
		
		verify(wishlistService, times(1)).findWishlistsOf(owner);
	}
	
	private Set<Wishlist> wishlists() {
		Set<Wishlist> wishlists = new HashSet<>();
		wishlists.add(publicWishlist());
		wishlists.add(emptyPublicWishlist());
		return wishlists;
	}
	
	private Wishlist publicWishlist() {
		Wishlist wishlist = new Wishlist("A", PUBLIC);
		wishlist.add(offer());
		return wishlist;
	}

	private Wishlist emptyPublicWishlist() {
		return new Wishlist("D", PUBLIC);
	}

	private Offer offer() {
		Offer offer = new Offer();
		offer.setName("test offer");
		return offer;
	}

	private User user() {
		User user = new User();
		user.setUserId("user");
		return user;
	}
	
	private User anotherUser() {
		User user = new User();
		user.setUserId("anotheruser");
		return user;
	}
	
}
