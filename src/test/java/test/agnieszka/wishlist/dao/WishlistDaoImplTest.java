package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.UserDao;
import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserState;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistState;

public class WishlistDaoImplTest extends EntityDaoImplTest{

	@Autowired
	private WishlistDao dao;
	
	@Autowired
	private UserDao userDao;
	
	private User wisher;
	private Wishlist wishlist;
	
	
	@Before
	public void setup() {
		wisher = getWisher();
		wishlist = getSampleWishlist();
		wishlist.setWisher(wisher);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findWishlistByIdInDb() {
		//given
		dao.save(wishlist);
		
		//when
		Wishlist wishlistFromDb = dao.findWishlistById(wishlist.getId());
		
		//then
		assertThat(wishlistFromDb).isEqualTo(wishlist);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findWishlistsForUser() {
		//given
		userDao.save(wisher);
		dao.save(wishlist);
		
		//when
		Set<Wishlist> wishlistsFromDb = dao.getUserWishlists(wisher);
		
		//then
		assertThat(wishlistsFromDb).contains(wishlist);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveWishlistInDb() {
		//when
		dao.save(wishlist);
		
		//then
		assertThat(dao.findWishlistById(wishlist.getId())).isEqualTo(wishlist);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateWishlistInDb() {
		//given
		dao.save(wishlist);
		wishlist.setName("ListaPróbnaPoZmianie");
		
		//when
		dao.update(wishlist);
		
		//then
		assertThat(dao.findWishlistById(wishlist.getId()).getName()).isEqualTo("ListaPróbnaPoZmianie");
	}
	
	private Wishlist getSampleWishlist() {
		return new Wishlist("ListaPróbna", WishlistState.PUBLIC);
	}
	
	private User getWisher() {
		EmailAddress email = new EmailAddress();
		email.setEmail("testowaniejava@gmail.com");
		User wisher = new User("testowy", "Użytkownik", "Testowy", email, UserState.ACTIVE);
		return wisher;
	}
}
