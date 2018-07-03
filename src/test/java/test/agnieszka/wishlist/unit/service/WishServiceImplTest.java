package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import agnieszka.wishlist.dao.WishDao;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.WishState;
import agnieszka.wishlist.service.WishServiceImpl;

public class WishServiceImplTest {

	@Mock
	private WishDao dao;
	
	@Spy
	private Wish wish;
	
	@Spy
	private List<Wish> wishs = new ArrayList<>();
	
	@InjectMocks
	private WishServiceImpl wishService;
	
	@Mock
	private User user;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		wish = getWish();
		wishs.add(wish);
	}
	
	@Test
	public void saveWishinDb() {
		//given
		doNothing().when(dao).save(any(Wish.class));
		
		//when
		wishService.save(any(Wish.class));
		
		//then
		verify(dao, times(1)).save(any(Wish.class));
	}
	
	@Test
	public void findAllWishInDb() {
		//given
		when(dao.getAllWishes()).thenReturn(wishs);
		
		//when
		List<Wish> foundedWishs = wishService.getAllWishes();
		
		//then
		verify(dao, times(1)).getAllWishes();
		assertThat(foundedWishs).hasSameElementsAs(wishs);
	}
	
	@Test
	public void findWishByIdInDb() {
		//given
		when(dao.findWishById(anyInt())).thenReturn(wish);
		
		//when
		Wish foundedWish = wishService.findWishById(anyInt());
		
		//then
		verify(dao, times(1)).findWishById(anyInt());
		assertThat(foundedWish).isEqualTo(wish);
	}
	
	@Test
	public void fulfilWish() {
		//when
		wishService.fulfilWish(wish, user);
		
		//then
		assertThat(wish.getState()).isEqualTo(WishState.PURCHASED);
		assertThat(wish.getFulfiller()).isEqualTo(user);
	}
	
	
	private Wish getWish() {
		Offer o = new Offer();
		o.setName("Oferta");
		return new Wish(o);
	}
}
