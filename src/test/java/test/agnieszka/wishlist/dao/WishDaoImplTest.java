package test.agnieszka.wishlist.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.dao.WishDao;
import agnieszka.wishlist.model.Wish;

public class WishDaoImplTest extends EntityDaoImplTest {

	@Autowired
	private WishDao dao;
	
	private Wish wish;
	
	@Before
	public void setup() {
		wish = getSampleWish();
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveWishInDb() {
		//when
		dao.save(wish);
		
		//then
		assertThat(dao.getAllWishes().size()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllWishInDb() {
		//given
		Wish wish2 = new Wish();
		
		dao.save(wish);
		dao.save(wish2);
		
		//when
		List<Wish> wishsFromDb = dao.getAllWishes();
		
		//then
		assertThat(wishsFromDb).contains(wish, wish2);
		assertThat(wishsFromDb.size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findWishByIdInDb() {
		//given
		dao.save(wish);
		
		//when
		Wish wishFromDb = dao.findWishById(wish.getId());
		
		//then
		assertThat(wishFromDb).isEqualTo(wish);
	}
	
	private Wish getSampleWish() {
		return new Wish();
	}
}
