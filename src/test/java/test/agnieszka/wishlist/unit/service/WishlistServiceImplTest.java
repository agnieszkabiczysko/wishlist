package test.agnieszka.wishlist.unit.service;

import static agnieszka.wishlist.model.WishlistState.PRIVATE;
import static agnieszka.wishlist.model.WishlistState.PUBLIC;
import static agnieszka.wishlist.model.WishlistState.SHARED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.converter.EmailFromString;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.service.UserPreferencesService;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceImplTest {

	@Mock
	private WishlistDao dao;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private UserService userService;
	
	@Mock
	private UserPreferencesService preferencesService;
	
	@Spy
	private Set<Wishlist> wishlists = new HashSet<>();
	
	@InjectMocks
	private WishlistServiceImpl wishlistService;

	@Mock
	private EmailFromString emailFromString;

	private Wishlist wishlist;
	
	private Offer o1;
	private Offer o2;
	
	private User user;
	
	@Before
	public void setup() {
		wishlist = getWishlist();
		wishlists.add(wishlist);
		user = getUser();
	}
	
	@Test
	public void findWishlistById() {
		//given
		when(dao.findWishlistById(anyInt())).thenReturn(wishlist);
		
		//when
		Wishlist wishlistFound = wishlistService.findWishlistById(anyInt());
		
		//then
		verify(dao, times(1)).findWishlistById(anyInt());
		assertThat(wishlistFound).hasSameClassAs(wishlist);
	}
	
	@Test
	public void findAllWishlistsForUser() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Set<Wishlist> wishlistsFound = wishlistService.findWishlistsOf(any(User.class));
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(wishlistsFound).hasSameElementsAs(wishlists);
	}
	
	@Test
	public void userWantsOffer() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Boolean userWants = wishlistService.userWantsOffer(user, o1);
		
		//then
		assertTrue(userWants);
	}
	
	@Test
	public void userDoesntWantOffer() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		Offer newOffer = new Offer();
		
		//when
		Boolean userWants = wishlistService.userWantsOffer(user, newOffer);
		
		//then
		assertFalse(userWants);
	}
	
	@Test
	public void saveWishlist() {
		//given
		doNothing().when(dao).save(any(Wishlist.class));
		
		//when
		wishlistService.save(any(Wishlist.class));
		
		//then
		verify(dao, times(1)).save(any(Wishlist.class));
	}
	
	@Test
	public void updateWishlist() {
		//given
		doNothing().when(dao).update(any(Wishlist.class));
		
		//when
		wishlistService.update(any(Wishlist.class));
		
		//then
		verify(dao, times(1)).update(any(Wishlist.class));
	}
	
	@Test
	public void sharedAndPublicWishlistsForUser() {
		//given
		Set<Wishlist> wishlists = new HashSet<>();
		Wishlist publicWishlist = getWishlist("A", PUBLIC);
		Wishlist anotherPublicWishlist = getWishlist("B", PUBLIC);
		Wishlist privateWishlist = getWishlist("C", PRIVATE);
		Wishlist sharedWishlist = getWishlist("D", SHARED);
		
		wishlists.add(publicWishlist);
		wishlists.add(privateWishlist);
		wishlists.add(sharedWishlist);
		wishlists.add(anotherPublicWishlist);
		
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Set<Wishlist> wishlistsFound = wishlistService.publicAndSharedWishlistsOf(user);
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(wishlistsFound).containsOnly(publicWishlist, anotherPublicWishlist, sharedWishlist);
	}
	
	@Test
	public void findPublicWishlists() {
		//given
		Set<Wishlist> wishlists = new HashSet<>();
		Wishlist publicWishlist = getWishlist("A", PUBLIC);
		Wishlist anotherPublicWishlist = getWishlist("B", PUBLIC);
		Wishlist privateWishlist = getWishlist("C", PRIVATE);
		Wishlist sharedWishlist = getWishlist("D", SHARED);
		
		wishlists.add(publicWishlist);
		wishlists.add(privateWishlist);
		wishlists.add(sharedWishlist);
		wishlists.add(anotherPublicWishlist);
		
		when(dao.getUserWishlists(user)).thenReturn(wishlists);

		//when
		Set<Wishlist> wishlistsFound = wishlistService.findPublicWishlistsOf(user);
		
		//then
		assertThat(wishlistsFound).containsOnly(publicWishlist, anotherPublicWishlist);
	}
	
	@Test
	public void testRetrieveCurrentWishList() {
		//given
		UserPreferences userPreferences = new UserPreferences(user, wishlist);
		when(preferencesService.findUserPreferencesForUser(user)).thenReturn(userPreferences);
		
		//when
		Wishlist currentWishlist = wishlistService.getCurrentWishlistOf(user);
		
		//then
		assertThat(currentWishlist).isEqualTo(wishlist);
	}
	
	@Test
	public void testStoringCurrentWishlist() {
		//when
		wishlistService.setCurrentWishlistOf(user, wishlist);
		
		//then
		verify(preferencesService, times(1)).updateCurrentWishlist(user, wishlist);
	}
	
	private Wishlist getWishlist() {
		Wishlist wishlist = new Wishlist();
		
		o1 = new Offer();
		o1.setName("Oferta1");
		
		o2 = new Offer();
		o2.setName("Oferta2");
		
		wishlist.add(o1);
		wishlist.add(o2);
		
		return wishlist;
	}
	
	private Wishlist getWishlist(String name, WishlistState state) {
		Wishlist wishlist = getWishlist();
		wishlist.setState(state.name());
		wishlist.setName(name);
		return wishlist;
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");;
		return user;
	}
}
