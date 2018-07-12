package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.ShowWishlistByMailIdController;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.WishlistMailService;

@RunWith(MockitoJUnitRunner.class)
public class ShowWishlistByMailIdControllerTest {

	@Mock
	private WishlistMailService mailService;
	
	@Mock
	private Principal principal;
	
	@InjectMocks
	private ShowWishlistByMailIdController controller;
	
	@Spy
	private ModelMap model;
	
	@Test
	public void showWishlistForExistingMailId() {
		//given
		String mailId = "xyz";
		
		User owner = user();
		Wishlist wishlist = wishlist(owner);
		
		when(mailService.findWishlistById(mailId)).thenReturn(wishlist);
		
		//when
		String viewName = controller.showWishlist(model, mailId);
		
		//then
		assertThat(viewName).isEqualTo("wishes");
		assertThat(model.get("wishes")).isEqualTo(wishlist.getWishes());
		assertThat(model.get("user")).isEqualTo(owner);
		assertThat(model.get("isWisher")).isEqualTo(false);
		assertThat(model.get("wishlistIsEmpty")).isEqualTo(false);
	}
	
	@Test
	public void forNonexistingMailIdShowError() {
		//given
		String mailId = "abcd";
		
		when(mailService.findWishlistById(mailId)).thenReturn(null);
		
		String errorMessage = "error message";
		ReflectionTestUtils.setField(controller, "invalidMailIdMessage", errorMessage);
		
		//when
		String viewName = controller.showWishlist(model, mailId);
		
		//then
		assertThat(viewName).isEqualTo("error");
		assertThat(model.get("message")).isEqualTo(errorMessage);
	}
	
	private Wishlist wishlist(User owner) {
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		
		Offer o2 = new Offer();
		o2.setName("Oferta2");
		
		Wishlist wishlist = new Wishlist();
		
		wishlist.setName("wishlist");
		wishlist.add(o1);
		wishlist.add(o2);

		wishlist.setWisher(owner);
		
		return wishlist;
	}
	
	private User user() {
		User user = new User();
		user.setUserId("U1");
		user.setId(1);
		return user;
	}

}
