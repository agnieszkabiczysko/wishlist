package test.agnieszka.wishlist.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.agnieszka.wishlist.dao.OfferDaoImplTest;
import test.agnieszka.wishlist.dao.RegisterMailDaoImplTest;
import test.agnieszka.wishlist.dao.UserDaoImplTest;
import test.agnieszka.wishlist.dao.UserPreferencesDaoImplTest;
import test.agnieszka.wishlist.dao.WishDaoImplTest;
import test.agnieszka.wishlist.dao.WishlistDaoImplTest;
import test.agnieszka.wishlist.dao.WishlistMailDaoImplTest;

@RunWith(Suite.class)

@SuiteClasses({
	OfferDaoImplTest.class,
	RegisterMailDaoImplTest.class,
	UserDaoImplTest.class,
	UserPreferencesDaoImplTest.class,
	WishDaoImplTest.class,
	WishlistDaoImplTest.class,
	WishlistMailDaoImplTest.class
})

public class DaoTestsSuite {

}
