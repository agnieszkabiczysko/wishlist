package test.agnieszka.wishlist.unit.controller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.ShowWishlistsController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.controller.helper.WishlistsHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ShowWishlistsControllerTest {

	@InjectMocks
	private ShowWishlistsController controller;
	
	@Mock
	private WishlistsHelper wishlistsHelper;
	
	@Mock
	private UserService userService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@SuppressWarnings("unchecked")
	@Test
	public void showMyWishlists() {
		//given
		Set<Wishlist> wishlists = wishlists(wishlist("B"), wishlist("A"));
		when(wishlistsHelper.wishlistsOf(user())).thenReturn(wishlists);
		
		mockUser(user());
		
		//when
		String view = controller.showWishlistsForUser(model, principal);
		
		//then
		assertThat(view).isEqualTo("wishlists");
		List<Wishlist> wishlistsFromModel = (List<Wishlist>) model.get("wishlists");
		assertThat(wishlistsFromModel).hasSameElementsAs(wishlists);
		assertThat(wishlistsFromModel).isSortedAccordingTo(Comparator.comparing(Wishlist::getName));
		verify(wishlistsHelper, times(1)).wishlistsOf(user());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void showGivenUserWishlists() {
		//given
		Set<Wishlist> wishlists = wishlists(wishlist("C"), wishlist("D"));
		when(wishlistsHelper.wishlistsOfVisibleFor(user(), anotherUser())).thenReturn(wishlists);
		when(userService.findUserById(user().getId())).thenReturn(user());
		
		mockUser(anotherUser());
		
		//when
		String view = controller.showWishlistsForSelectedUser(model, user().getId(), principal);
		
		//then
		assertThat(view).isEqualTo("wishlists");
		assertThat((Set<Wishlist>) model.get("wishlists")).containsExactlyElementsOf(wishlists);
		assertThat(model.get("user")).isEqualTo(user().getUserId());
		assertThat(model.get("search")).isEqualTo(true);
		verify(userService, times(1)).findUserById(user().getId());
		verify(wishlistsHelper, times(1)).wishlistsOfVisibleFor(user(), anotherUser());
	}
	
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
	}
	
	private Set<Wishlist> wishlists(Wishlist... wishlists) {
		Set<Wishlist> wishlistsSet = new HashSet<>();
		asList(wishlists).stream().forEach(wishlistsSet::add);
		return wishlistsSet;
	}

	private Wishlist wishlist(String name) {
		Wishlist wishlist = new Wishlist();
		wishlist.setName(name);
		return wishlist;
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
