package test.agnieszka.wishlist.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.agnieszka.wishlist.unit.controller.FriendControllerTest;
import test.agnieszka.wishlist.unit.controller.OfferControllerTest;
import test.agnieszka.wishlist.unit.controller.RegisterControllerTest;
import test.agnieszka.wishlist.unit.controller.SecurityControllerTest;
import test.agnieszka.wishlist.unit.controller.WishControllerTest;
import test.agnieszka.wishlist.unit.controller.WishlistControllerTest;

@RunWith(Suite.class)

@SuiteClasses({
	FriendControllerTest.class,
	OfferControllerTest.class,
	RegisterControllerTest.class,
	SecurityControllerTest.class,
	WishControllerTest.class,
	WishlistControllerTest.class,
})

public class ControllerTestsSuite {

}
