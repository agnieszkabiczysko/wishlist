package test.agnieszka.wishlist.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.WishController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.WishState;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishService;

@RunWith(MockitoJUnitRunner.class)
public class WishControllerTest {

	@Mock
	private WishService wishService;

	@Mock
	private UserService userService;
	
	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@InjectMocks
	private WishController controller;
	
	@Mock
	private Principal principal;
	
	@Spy
	private List<Wish> wishes = new ArrayList<>();

	private User user;
	private User secondUser;
	
	@Spy
	private ModelMap model;
	
	@Before
	public void setup() {
		user = getUser();
		secondUser = getSecondUser();
		wishes = getWishes();
	}
	
	@Test
	public void showWishForNotWisher() {
		//given
		Wish wish = wishes.get(0);
		when(wishService.findWishById(anyInt())).thenReturn(wish);
		setCurrentUser(secondUser);
		
		//when
		String viewName = controller.showWish(model, wish.getId(), principal);
		
		//then
		assertEquals("wish", viewName);
		assertEquals(wish, model.get("wish"));
		assertEquals(true, model.get("canBuy"));
		verify(wishService, times(1)).findWishById(anyInt());
	}
	
	@Test
	public void showWishForWisher() {
		//given
		Wish wish = wishes.get(0);
		when(wishService.findWishById(anyInt())).thenReturn(wish);
		setCurrentUser(user);
		
		//when
		String viewName = controller.showWish(model, wish.getId(), principal);
		
		//then
		assertEquals("wish", viewName);
		assertEquals(wish, model.get("wish"));
		assertEquals(false, model.get("canBuy"));
		verify(wishService, times(1)).findWishById(anyInt());
	}
	
	@Test
	public void showWishThatIsPurchased() {
		//given
		Wish wish = wishes.get(0);
		wish.setState(WishState.PURCHASED);
		when(wishService.findWishById(anyInt())).thenReturn(wish);
		setCurrentUser(user);
		
		//when
		String viewName = controller.showWish(model, wish.getId(), principal);
		
		//then
		assertEquals("wish", viewName);
		assertEquals(wish, model.get("wish"));
		assertEquals(true, model.get("isPurchased"));
		verify(wishService, times(1)).findWishById(anyInt());
	}
	
	@Test
	public void userFulfilsAWish() {
		//given
		Wish wish = wishes.get(0);
		when(wishService.findWishById(anyInt())).thenReturn(wish);
		setCurrentUser(user);
		doNothing().when(wishService).fulfilWish(wish, user);
		
		//when
		String viewName = controller.fulfilWish(wish.getId(), principal);
		
		//then
		assertEquals("redirect:/offers", viewName);
	}
	
	
	private void setCurrentUser(User user) {
		when(currentUserHelper.getCurrentUser(any(Principal.class))).thenReturn(user);
	}
	
	private List<Wish> getWishes() {
		Wishlist wishlist = new Wishlist();
		wishlist.setWisher(user);
		
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		Wish wish1 = new Wish(o1);
		wish1.setState(WishState.PURCHASED);
		wish1.setWishlist(wishlist);
		
		Offer o2 = new Offer();
		o1.setName("Oferta2");
		Wish wish2 = new Wish(o2);
		wish2.setWishlist(wishlist);
		
		wishes.add(wish1);
		wishes.add(wish2);
		
		return wishes;
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");;
		return user;
	}

	private User getSecondUser() {
		User user = new User();
		user.setUserId("U2");
		return user;
	}
}
