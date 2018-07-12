package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.controller.ShareWishlistController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.ShareWishlistEmailSendService;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistService;

@RunWith(MockitoJUnitRunner.class)
public class ShareWishlistControllerTest {

	@InjectMocks
	private ShareWishlistController controller;
	
	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ShareWishlistEmailSendService sendEmailService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private BindingResult result;
	
	@Mock
	private Principal principal;
	
	@Test
	public void withValidationErrors() {
		//given
		EmailAddress emailAddress = new EmailAddress("test@test.com");
		
		when(result.hasErrors()).thenReturn(true);
		when(wishlistService.findWishlistById(1)).thenReturn(mock(Wishlist.class));

		//when
		String view = controller.shareWishlist(1, emailAddress, result, model, principal);
		
		//then
		assertThat(view).isEqualTo("wishes");
		assertThat(model.get("error")).isEqualTo(true);
		assertThat(model.get("shareEmail")).isEqualTo(emailAddress);
		assertThat(model.get("wishlistId")).isEqualTo(1);
	}
	
	@Test
	public void successfulShare() {
		//given
		EmailAddress emailAddress = new EmailAddress("test@test.com");
		Wishlist wishlist = wishlist();
		User currentUser = anotherUser();
		
		when(result.hasErrors()).thenReturn(false);
		
		when(wishlistService.findWishlistById(2)).thenReturn(wishlist);
		
		when(userService.findUserByEmail(emailAddress)).thenReturn(user());
		
		mockUser(currentUser);
		
		//when
		String view = controller.shareWishlist(2, emailAddress, result, model, principal);
		
		//then
		assertThat(view).isEqualTo("redirect:/myWishlists");
		assertThat(user().isFriendOf(currentUser)).isEqualTo(true);
		
		verify(sendEmailService, times(1)).shareWishlistViaEmail(wishlist, emailAddress);
		verify(userService, times(1)).update(currentUser);
	}
	
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
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
