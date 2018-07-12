package test.agnieszka.wishlist.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.agnieszka.wishlist.unit.service.ConfirmationMailServiceImplTest;
import test.agnieszka.wishlist.unit.service.OfferServiceImplTest;
import test.agnieszka.wishlist.unit.service.PasswordServiceImplTest;
import test.agnieszka.wishlist.unit.service.RegisterMailServiceImplTest;
import test.agnieszka.wishlist.unit.service.ShareWishlistEmailSendServiceTest;
import test.agnieszka.wishlist.unit.service.UserPreferencesServiceImplTest;
import test.agnieszka.wishlist.unit.service.UserServiceImplTest;
import test.agnieszka.wishlist.unit.service.WishServiceImplTest;
import test.agnieszka.wishlist.unit.service.WishlistMailServiceImplTest;
import test.agnieszka.wishlist.unit.service.WishlistServiceImplTest;

@RunWith(Suite.class)

@SuiteClasses({
	ConfirmationMailServiceImplTest.class,
	OfferServiceImplTest.class,
	PasswordServiceImplTest.class,
	RegisterMailServiceImplTest.class,
	ShareWishlistEmailSendServiceTest.class,
	UserPreferencesServiceImplTest.class,
	UserServiceImplTest.class,
	WishlistMailServiceImplTest.class,
	WishlistServiceImplTest.class,
	WishServiceImplTest.class
})

public class ServiceTestsSuite {

}
