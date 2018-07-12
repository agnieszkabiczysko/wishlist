package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.dao.WishlistMailDao;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.service.WishlistMailServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class WishlistMailServiceImplTest {

	@Mock
	private WishlistMailDao dao;
	
	@Mock
	private Wishlist wishlist;
	
	@InjectMocks
	private WishlistMailServiceImpl mailService;
	
	@Spy
	private WishlistMail wishlistMail;
	
	@Test
	public void saveWishlistMail() {
		//given
		doNothing().when(dao).saveWishlistMail(any(WishlistMail.class));
		
		//when
		mailService.saveWishlistMail(any(WishlistMail.class));
		
		//then
		verify(dao, times(1)).saveWishlistMail(any(WishlistMail.class));
	}
	
	@Test
	public void findWishlistById() {
		//given
		when(dao.findWishlistMailById(anyString())).thenReturn(null);
		
		//when
		Wishlist wishlistFound = mailService.findWishlistById(anyString());
		
		//then
		verify(dao, times(1)).findWishlistMailById(anyString());
		assertThat(wishlistFound).isNull();
	}
	
	@Test
	public void findWishlistByIdWithoutException() {
		//given
		when(dao.findWishlistMailById(anyString())).thenReturn(wishlistMail);
		wishlistMail.setWishlist(wishlist);
		
		//when
		Wishlist wishlistFound = mailService.findWishlistById(anyString());
		
		//then
		verify(dao, times(1)).findWishlistMailById(anyString());
		assertThat(wishlistFound).isEqualTo(wishlist);
	}
	
	@Test
	public void createWishlistMail() {
		//given
		doNothing().when(dao).saveWishlistMail(any(WishlistMail.class));
		
		//when
		WishlistMail wishlistMail = mailService.createWishlistMail(any(Wishlist.class)); 
		
		//then
		verify(dao, times(1)).saveWishlistMail(any(WishlistMail.class));
		assertThat(wishlistMail).isInstanceOf(WishlistMail.class);
	}
}
