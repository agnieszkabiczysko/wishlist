package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishlistDao;
import agnieszka.wishlist.dao.WishlistMailDao;
import agnieszka.wishlist.dao.WishlistMailDaoImpl;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.model.WishlistState;

public class WishlistMailDaoImplTest extends EntityDaoImplTest {

	@Autowired
	private WishlistMailDao dao = new WishlistMailDaoImpl();
	
	@Autowired
	private WishlistDao wishlistDao;
	
	private WishlistMail wishlistMail;
	
	@Before
	public void setup() {
		Wishlist wishlist = new Wishlist("ListaPróbna", WishlistState.PUBLIC);
		wishlistDao.save(wishlist);
		wishlistMail = new WishlistMail("mailId", wishlist);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveWishlistMail() {
		//when
		dao.saveWishlistMail(wishlistMail);
		
		//then
		assertThat(dao.findWishlistMailById(wishlistMail.getMailId())).isEqualTo(wishlistMail);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findWishlistMailById() {
		//given
		dao.saveWishlistMail(wishlistMail);
		
		//when
		WishlistMail wishlistMailFromDb = dao.findWishlistMailById(wishlistMail.getMailId());
		
		//then
		assertThat(wishlistMailFromDb).isEqualTo(wishlistMail);
	}
}
