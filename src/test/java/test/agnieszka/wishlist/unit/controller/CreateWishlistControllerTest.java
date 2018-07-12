package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.CreateWishlistController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class CreateWishlistControllerTest {

	@InjectMocks
	private CreateWishlistController controller;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@Test
	public void showNewWishlistForm() {
		//given
		Wishlist wishlist = wishlist();
		
		//when
		String view = controller.showCreateWishlistForm(model, wishlist);
		
		//then
		assertThat(view).isEqualTo("createWishlist");
		assertThat(model.get("wishlist")).isEqualTo(wishlist);
	}
	
	@Test
	public void createNewWishlist() {
		//given
		Wishlist wishlist = wishlist();
		
		mockUser(user());
		
		//when
		String view = controller.processCreateWishlistForm(wishlist, model, principal);
		
		//then
		assertThat(view).isEqualTo("redirect:/myWishlists");
		assertThat(wishlist.getWisher()).isEqualTo(user());
		verify(wishlistService, times(1)).save(wishlist);
	}
	
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
	}
	
	private Wishlist wishlist() {
		Wishlist wishlist = new Wishlist();
		wishlist.setName("test wishlist");
		return wishlist;
	}
	
	private User user() {
		User user = new User();
		user.setUserId("user");
		return user;
	}

}
