package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import agnieszka.wishlist.converter.EmailFromString;
import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.model.WishlistTermType;
import agnieszka.wishlist.service.UserService;
import agnieszka.wishlist.service.WishlistServiceImpl;

public class WishlistServiceImplTest {

	@Mock
	private WishlistDao dao;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private UserService userService;
	
	@Spy
	private Wishlist wishlist;
	
	@Spy
	private Set<Wishlist> wishlists = new HashSet<>();
	
	@InjectMocks
	private WishlistServiceImpl wishlistService;

	private User user;
	
	@Mock
	private EmailFromString emailFromString;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		wishlist = getWishlist();
		wishlists.add(wishlist);
		user = getUser();
	}
	
	@Test
	public void findWishlistById() {
		//given
		when(dao.findWishlistById(anyInt())).thenReturn(wishlist);
		
		//when
		Wishlist foundedWishlist = wishlistService.findWishlistById(anyInt());
		
		//then
		verify(dao, times(1)).findWishlistById(anyInt());
		assertThat(foundedWishlist).hasSameClassAs(wishlist);
	}
	
	@Test
	public void findAllWishlistsForUser() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService.getAllWishlistsForUser(any(User.class));
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	@Test
	public void userWantsOffer() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		Offer offer = wishlists.iterator().next().getWishes().iterator().next().getOffer();
		
		//when
		Boolean ifWants = wishlistService.userWantsOffer(user, offer);
		
		//then
		assertTrue(ifWants);
	}
	
	@Test
	public void userDoesntWantOffer() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		Offer newOffer = new Offer();
		
		//when
		Boolean ifWants = wishlistService.userWantsOffer(user, newOffer);
		
		//then
		assertFalse(ifWants);
	}
	
	@Test
	public void saveWishlistInDb() {
		//given
		doNothing().when(dao).save(any(Wishlist.class));
		
		//when
		wishlistService.save(any(Wishlist.class));
		
		//then
		verify(dao, times(1)).save(any(Wishlist.class));
	}
	
	@Test
	public void updateWishlistInDb() {
		//given
		doNothing().when(dao).update(any(Wishlist.class));
		
		//when
		wishlistService.update(any(Wishlist.class));
		
		//then
		verify(dao, times(1)).update(any(Wishlist.class));
	}
	
	@Test
	public void findWishlistsIfTermTypeEqualsUser() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService.findWishlistsByTerm(WishlistTermType.USERID, anyString());
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	@Test
	public void findWishlistsifTermTypeEqualsEmail() {
		//given
		when(userDao.findUserByEmail(any(EmailAddress.class))).thenReturn(user);
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService.findWishlistsByTerm(WishlistTermType.EMAIL, anyString());
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	
	@Test
	public void findPublicWishlists() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		wishlists.iterator().next().setState("PUBLIC");
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService
				.findWishlistsByTermAndState(WishlistTermType.USERID, anyString(), WishlistState.PUBLIC);
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}

	@Test
	public void findSharedWishlists() {
		//given
		when(userService.findUserByUserId(anyString())).thenReturn(user);
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		wishlists.iterator().next().setState("SHARED");
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService
				.findWishlistsByTermAndState(WishlistTermType.USERID, anyString(), WishlistState.SHARED);
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	@Test
	public void findPrivateWishlists() {
		//given
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		wishlists.iterator().next().setState("PRIVATE");
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService
				.findWishlistsByTermAndState(WishlistTermType.USERID, anyString(), WishlistState.PRIVATE);
				
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	@Test
	public void sharedAndPublicWishlistsForUser() {
		//given
		wishlists.iterator().next().setState("SHARED");
		
		Wishlist newWishlist = new Wishlist();
		newWishlist.setState("PUBLIC");
		wishlists.add(newWishlist);
		
		when(dao.getUserWishlists(any(User.class))).thenReturn(wishlists);
		
		User friend = new User();
		HashSet<User> friends = new HashSet<>();
		friends.add(friend);
		user.setFriends(friends);
		
		//when
		Set<Wishlist> foundedWishlists = wishlistService.sharedAndPublicWishlistsForUser(user, friend);
		
		//then
		verify(dao, times(1)).getUserWishlists(any(User.class));
		assertThat(foundedWishlists).hasSameElementsAs(wishlists);
	}
	
	private Wishlist getWishlist() {
		Offer o1 = new Offer();
		o1.setName("Oferta1");
		Wish w1 = new Wish(o1);
		
		Offer o2 = new Offer();
		o2.setName("Oferta2");
		Wish w2 = new Wish(o2);
		
		wishlist.add(w1);
		wishlist.add(w2);
		
		return wishlist;
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");;
		return user;
	}
}
