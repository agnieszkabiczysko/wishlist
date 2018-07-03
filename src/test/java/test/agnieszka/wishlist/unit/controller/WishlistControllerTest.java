package test.agnieszka.wishlist.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.controller.WishlistController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.exception.InvalidWishlistMailIdException;
import agnieszka.wishlist.formatter.WishFormatter;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.model.WishlistTermType;
import agnieszka.wishlist.service.OfferService;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistMailService;
import agnieszka.wishlist.service.WishlistService;

public class WishlistControllerTest {

	@Mock
	private WishlistService wishlistService;
	
	@Mock
	private UserService userService;

	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Mock
	private OfferService offerService;
	
	@Mock
	private UserPreferencesService preferencesService;
	
	@Mock
	private WishlistMailService mailService;
	
	@Mock
	private Principal principal;
	
	@InjectMocks
	private WishlistController controller;
	
	@Mock
	private ApplicationMailer mailer;
	
	@Mock
	private WishFormatter formatter;
	
	@Spy
	private Wishlist wishlist;
	
	@Spy
	private Wishlist emptyWishlist = new Wishlist();
	
	@Spy
	private Set<Wishlist> wishlists = new HashSet<>();
	
	@Spy
	private ModelMap model;
	
	private User user;
	private User user2;
	
	@Mock
	private EmailAddress mail;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private BindingResult result;
	
	@Mock
	private WishlistMail wishlistMail;
	
	@Mock
	private UrlHelper urlHelper;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		wishlist = getWishlist();
		wishlists.add(wishlist);
		user = getUser();
		user2 = getSecondUser();
	}
	
	@Test
	public void showAllWishesForWishlistForNoWisher() {
		//given
		EmailAddress email = new EmailAddress();
		when(wishlistService.findWishlistById(anyInt())).thenReturn(wishlist);
		mockUser(user);
		when(wishlist.getWisher()).thenReturn(user2);
		
		//when
		String viewName = controller.showAllWishesForWishlist(0, model, email, principal);
		
		//then
		assertEquals("wishes", viewName);
		assertEquals(wishlist.getWisher(), model.get("user"));
		assertEquals(wishlist.getWishes(), model.get("wishes"));
	}
	
	@Test
	public void showAllWishesForWishlistForWisher() {
		//given
		EmailAddress email = new EmailAddress();
		when(wishlistService.findWishlistById(anyInt())).thenReturn(wishlist);
		mockUser(user);
		when(wishlist.getWisher()).thenReturn(user);
		
		//when
		String viewName = controller.showAllWishesForWishlist(0, model, email, principal);
		
		//then
		assertEquals("wishes", viewName);
		assertEquals(wishlist.getWisher(), model.get("user"));
		assertEquals(wishlist.getWishes(), model.get("wishes"));
		assertEquals(true, model.get("isWisher"));
	}
	
	@Test
	public void showAllWishesForWishlistIfWishlistIsEmpty() {
		//given
		EmailAddress email = new EmailAddress();
		when(wishlistService.findWishlistById(anyInt())).thenReturn(emptyWishlist);
		mockUser(user);
		when(emptyWishlist.getWisher()).thenReturn(user2);
		
		//when
		String viewName = controller.showAllWishesForWishlist(0, model, email, principal);
		
		//then
		assertEquals("wishes", viewName);
		assertEquals(true, model.get("wishlistIsEmpty"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void showWishlistsForUser() {
		//given
		mockUser(user);
		when(wishlistService.getAllWishlistsForUser(user)).thenReturn(wishlists);
		
		//when
		String viewName = controller.showWishlistsForUser(model, principal);
		
		//then
		assertEquals("wishlists", viewName);
		assertThat(wishlists).hasSameElementsAs((Set<Wishlist>) model.get("wishlists"));
	}
	
	@Test
	public void showWishlistsForSelectedUser() {
		//given
		mockUser(user);
		when(userService.findUserById(anyInt())).thenReturn(user2);
		when(wishlistService.sharedAndPublicWishlistsForUser(user2, user)).thenReturn(wishlists);
		
		//when
		String viewName = controller.showWishlistsForSelectedUser(model, 0, principal);
		
		//then
		assertEquals("wishlists", viewName);
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(true, model.get("search"));
		assertEquals(user2.getUserId(), model.get("user"));
	}
	
	@Test
	public void chooseWishlist() {
		//given
		Offer offer = wishlist.getWishes().iterator().next().getOffer();
		when(offerService.findOfferById(anyInt())).thenReturn(offer);
		when(wishlistService.findWishlistById(anyInt())).thenReturn(wishlist);
		mockUser(user);
		doNothing().when(wishlistService).update(any(Wishlist.class));
		
		//when
		String viewName = controller.chooseWishlist(0, 0, principal);
		
		//then
		assertEquals("redirect:/myWishlists", viewName);
	}
	
	@Test
	public void markOfferAsWish() {
		//given
		Offer offer = wishlist.getWishes().iterator().next().getOffer();
		when(userService.getCurrentWishlist(any(User.class))).thenReturn(wishlist);
		when(offerService.findOfferById(anyInt())).thenReturn(offer);
		mockUser(user);
		doNothing().when(wishlistService).update(any(Wishlist.class));
		
		//when
		String viewName = controller.markOfferAsWish(0, principal);
		
		//then
		assertEquals("redirect:/myWishlists", viewName);
	}
	
	
	@Test
	public void showCreateWishlistForm() {
		//when
		String viewName = controller.showCreateWishlistForm(model, wishlist);
		
		//then
		assertEquals("createWishlist", viewName);
		assertEquals(wishlist, model.get("wishlist"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void processCreateWishlistForm() {
		//given
		mockUser(user);
		doNothing().when(wishlistService).save(any(Wishlist.class));
		when(wishlistService.getAllWishlistsForUser(any(User.class))).thenReturn(wishlists);
		
		//when
		String viewName = controller.processCreateWishlistForm(wishlist, principal, model);
		
		//then
		assertEquals("wishlists", viewName);
		assertThat(wishlists).hasSameElementsAs((Set<Wishlist>) model.get("wishlists"));
	}
	
	@Test
	public void showSearchWishlistForm() {
		//when
		String viewName = controller.showSearchWishlistForm();
		
		//then
		assertEquals("findWishlist", viewName);
	}
	
	@Test
	public void showSearchWishlistResultsForLogoutUser() {
		//given
		when(wishlistService.findWishlistsByTermAndState(WishlistTermType.USERID, "U1", WishlistState.PUBLIC)).thenReturn(wishlists);
		
		//when
		String viewName = controller.showSearchWishlistResults(model, "U1", WishlistTermType.USERID.toString(), null);
		
		//then
		assertEquals("wishlists", viewName);
		assertEquals(true, model.get("search"));
	}
	
	@Test
	public void showSearchWishlistResultsForLoginUser() {
		//given
		mockUser(user);
		when(wishlistService.findWishlistsByTerm(WishlistTermType.USERID, "U2")).thenReturn(wishlists);
		when(userService.findUserByUserId(anyString())).thenReturn(user2);
		when(wishlistService.sharedAndPublicWishlistsForUser(user2, user)).thenReturn(wishlists);
		
		//when
		String viewName = controller.showSearchWishlistResults(model, "U2", WishlistTermType.USERID.toString(), principal);
		
		//then
		assertEquals("wishlists", viewName);
		assertEquals(true, model.get("search"));
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(user2.getUserId(), model.get("user"));
	}
	
	@Test
	public void showSearchWishlistResultsForUserWhichIsWisher() {
		//given
		mockUser(user);
		when(wishlistService.findWishlistsByTerm(WishlistTermType.USERID, "U1")).thenReturn(wishlists);
		when(userService.findUserByUserId(anyString())).thenReturn(user);
		
		//when
		String viewName = controller.showSearchWishlistResults(model, "U1", WishlistTermType.USERID.toString(), principal);
		
		//then
		assertEquals("wishlists", viewName);
		assertEquals(true, model.get("search"));
		assertEquals(wishlists, model.get("wishlists"));
		assertEquals(user.getUserId(), model.get("user"));
	}
	
	@Test
	public void showSearchWishlistResultsOfEmptyWishlistForLogoutUser() {
		//when
		String viewName = controller.showSearchWishlistResults(model, "test", WishlistTermType.USERID.toString(), null);
		
		//then
		assertEquals("noResultsFound", viewName);
	}
	
	@Test
	public void showSearchWishlistResultsOfEmptyWishlistForLoginUser() {
		//given
		Set<Wishlist> emptyWishlists = new HashSet<>();
		mockUser(user);
		when(wishlistService.findWishlistsByTerm(WishlistTermType.USERID, "U1")).thenReturn(emptyWishlists);
		when(userService.findUserByUserId(anyString())).thenReturn(user);
		
		//when
		String viewName = controller.showSearchWishlistResults(model, "U1", WishlistTermType.USERID.toString(), principal);
		
		//then
		assertEquals("noResultsFound", viewName);
	}
	
	@Test
	public void showWishlistMailWithoutException() throws InvalidWishlistMailIdException {
		//given
		when(mailService.findWishlistById(anyString())).thenReturn(wishlist);
		
		//when
		String viewName = controller.showWishlistMail(model, anyString());
		
		//then
		assertEquals("wishes", viewName);
		assertEquals(wishlist.getWishes(), model.get("wishes"));
		assertEquals(user, model.get("user"));
		assertEquals(false, model.get("isWisher"));
		assertEquals(false, model.get("wishlistIsEmpty"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void showExceptionPageAfterDeliveringInvalidWishlistId() throws InvalidWishlistMailIdException {
		//given
		when(mailService.findWishlistById(anyString())).thenThrow(InvalidWishlistMailIdException.class);
		
		//when
		String viewName = controller.showWishlistMail(model, anyString());
		
		//then
		assertEquals("error", viewName);
	}
	
	@Test
	public void shareWishesWithoutSuccess() {
		//given
		when(result.hasErrors()).thenReturn(true);
		when(wishlistService.findWishlistById(anyInt())).thenReturn(wishlist);
		mockUser(user);
		
		//when
		String viewName = controller.shareWishes(0, mail, result, request, principal, model);
		
		//then
		assertEquals("wishes", viewName);
		assertEquals(true, model.get("error"));
		
	}
	
	@Test
	public void shareWishesWithSuccess() {
		//given
		when(result.hasErrors()).thenReturn(false);
		when(wishlistService.findWishlistById(anyInt())).thenReturn(wishlist);
		when(mailService.createWishlistMail(any(Wishlist.class))).thenReturn(wishlistMail);
		when(formatter.formatWishlistForEmail(wishlist.getWishes())).thenReturn("");
		when(urlHelper.createRegisterUrl(request)).thenReturn("http://localhost:8080/wishist/wishlist/");
		doNothing().when(mailer).sendMessage(any(EmailAddress.class), anyString(), anyString());
		mockUser(user);
		when(userService.findUserByEmail(any(EmailAddress.class))).thenReturn(user2);
		user.addFriend(user2);
		doNothing().when(userService).update(user);
		
		//when
		String viewName = controller.shareWishes(0, mail, result, request, principal, model);
		
		//then
		assertEquals("redirect:/myWishlists", viewName);
	}
	private void mockUser(User user) {
		when(currentUserHelper.getCurrentUser(any(Principal.class))).thenReturn(user);
	}
	
	private Wishlist getWishlist() {
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		Wish w1 = new Wish(o1);
		
		Offer o2 = new Offer();
		o2.setName("Oferta2");
		Wish w2 = new Wish(o2);
		
		wishlist.setName("wishlist");
		wishlist.add(w1);
		wishlist.add(w2);

		wishlist.setWisher(getUser());
		
		return wishlist;
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");
		user.setId(1);
		return user;
	}

	private User getSecondUser() {
		User user = new User();
		user.setUserId("U2");
		user.setId(2);
		return user;
	}
}
