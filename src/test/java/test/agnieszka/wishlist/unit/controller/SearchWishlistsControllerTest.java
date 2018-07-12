package test.agnieszka.wishlist.unit.controller;

import static agnieszka.wishlist.model.WishlistTermType.EMAIL;
import static agnieszka.wishlist.model.WishlistTermType.USERID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.SearchWishlistsController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.controller.helper.WishlistsHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class SearchWishlistsControllerTest {

	@InjectMocks
	private SearchWishlistsController controller;
	
	@Mock
	private UserService userService;
	
	@Mock
	private WishlistsHelper wishlistsHelper;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@Test
	public void showSearchForm() {
		//when
		String view = controller.showSearchForm();
		
		//then
		assertThat(view).isEqualTo("findWishlist");
	}
	
	@Test
	public void searchWishlistsByUserName() {
		//given
		String term = "user";
		String termTypeStr = USERID.name();
		
		Set<Wishlist> wishlists = wishlists();
		
		when(wishlistsHelper.wishlistsOfVisibleFor(user(), anotherUser())).thenReturn(wishlists);
		when(userService.findUserByUserId(term)).thenReturn(user());
		
		mockUser(anotherUser());
		
		//when
		String view = controller.showSearchWishlistResults(model, term, termTypeStr, principal);
		
		//then
		assertThat(view).isEqualTo("wishlists");
		assertThat(model.get("wishlists")).isEqualTo(wishlists);
		assertThat(model.get("search")).isEqualTo(true);
		assertThat(model.get("term")).isEqualTo(term);
		verify(userService, times(1)).findUserByUserId(term);
		verify(userService, never()).findUserByEmail(anyString());
		verify(wishlistsHelper, times(1)).wishlistsOfVisibleFor(user(), anotherUser());
	}

	@Test
	public void searchWishlistsByEmail() {
		//given
		String term = "email";
		String termTypeStr = EMAIL.name();
		
		Set<Wishlist> wishlists = wishlists();
		
		when(wishlistsHelper.wishlistsOfVisibleFor(user(), anotherUser())).thenReturn(wishlists);
		when(userService.findUserByEmail(term)).thenReturn(user());
		
		mockUser(anotherUser());
		
		//when
		String view = controller.showSearchWishlistResults(model, term, termTypeStr, principal);
		
		//then
		assertThat(view).isEqualTo("wishlists");
		assertThat(model.get("wishlists")).isEqualTo(wishlists);
		assertThat(model.get("search")).isEqualTo(true);
		assertThat(model.get("term")).isEqualTo(term);
		verify(userService, times(1)).findUserByEmail(anyString());
		verify(userService, never()).findUserByUserId(term);
		verify(wishlistsHelper, times(1)).wishlistsOfVisibleFor(user(), anotherUser());
	}
	
	@Test
	public void emptySearchResults() {
		String term = "user";
		String termTypeStr = USERID.name();
		
		Set<Wishlist> wishlists = new HashSet<>();
		
		when(wishlistsHelper.wishlistsOfVisibleFor(user(), anotherUser())).thenReturn(wishlists);
		when(userService.findUserByUserId(term)).thenReturn(user());
		
		mockUser(anotherUser());
		
		//when
		String view = controller.showSearchWishlistResults(model, term, termTypeStr, principal);
		
		//then
		assertThat(view).isEqualTo("noResultsFound");
		assertThat(model.get("wishlists")).isEqualTo(wishlists);
		assertThat(model.get("search")).isEqualTo(true);
		assertThat(model.get("term")).isEqualTo(term);
	}

	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
	}
	
	private Set<Wishlist> wishlists() {
		Set<Wishlist> wishlists = new HashSet<>();
		wishlists.add(wishlist());
		return wishlists;
	}

	private Wishlist wishlist() {
		Wishlist wishlist = new Wishlist();
		wishlist.setName("V");
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
