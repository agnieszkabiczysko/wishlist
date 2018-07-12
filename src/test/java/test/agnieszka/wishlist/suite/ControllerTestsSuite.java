package test.agnieszka.wishlist.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.agnieszka.wishlist.unit.controller.AddToWishlistControllerTest;
import test.agnieszka.wishlist.unit.controller.ConfirmRegistrationControllerTest;
import test.agnieszka.wishlist.unit.controller.CreateWishlistControllerTest;
import test.agnieszka.wishlist.unit.controller.FriendControllerTest;
import test.agnieszka.wishlist.unit.controller.ModifyOfferControllerTest;
import test.agnieszka.wishlist.unit.controller.RegisterControllerTest;
import test.agnieszka.wishlist.unit.controller.SearchOfferControllerTest;
import test.agnieszka.wishlist.unit.controller.SearchWishlistsControllerTest;
import test.agnieszka.wishlist.unit.controller.SecurityControllerTest;
import test.agnieszka.wishlist.unit.controller.ShareWishlistControllerTest;
import test.agnieszka.wishlist.unit.controller.ShowOfferControllerTest;
import test.agnieszka.wishlist.unit.controller.ShowWishlistByMailIdControllerTest;
import test.agnieszka.wishlist.unit.controller.ShowWishlistControllerTest;
import test.agnieszka.wishlist.unit.controller.ShowWishlistsControllerTest;
import test.agnieszka.wishlist.unit.controller.WishControllerTest;

@RunWith(Suite.class)

@SuiteClasses({
	AddToWishlistControllerTest.class,
	ConfirmRegistrationControllerTest.class,
	CreateWishlistControllerTest.class,
	FriendControllerTest.class,
	ModifyOfferControllerTest.class,
	RegisterControllerTest.class,
	SearchOfferControllerTest.class,
	SearchWishlistsControllerTest.class,
	SecurityControllerTest.class,
	ShareWishlistControllerTest.class,
	ShowOfferControllerTest.class,
	ShowWishlistByMailIdControllerTest.class,
	ShowWishlistControllerTest.class,
	ShowWishlistsControllerTest.class,
	WishControllerTest.class,
})

public class ControllerTestsSuite {

}
