package test.agnieszka.wishlist.endToEnd;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import test.agnieszka.wishlist.email.GmailInbox;
import test.agnieszka.wishlist.selenium.SeleniumRunner;
import test.agnieszka.wishlist.ui.ApplicationActivityVerificator;
import test.agnieszka.wishlist.ui.SeleniumRunnerData;


public class AllEndToEndTest {

	static {
		System.setProperty("webdriver.gecko.driver", "/home/agnieszka/dev/selenium/lib/geckodriver");
	}
	private static String baseUrl = "http://localhost:8080/wishlist/";
	private static WebDriver driver = new FirefoxDriver();
	
	private final static SeleniumRunner application = new SeleniumRunner(driver);
	private final static ApplicationActivityVerificator verificator = new ApplicationActivityVerificator(driver);
	private final static SeleniumRunnerData data = new SeleniumRunnerData();
	private final static GmailInbox inbox = new GmailInbox();

	@Test
	public void registerTest() {
		application.register(data.testUserName(), data.firstnameTestUser(), data.lastnameTestUser(), data.emailForTestUser());
		application.clickLink(inbox.extractLink
							(inbox.getLatestMessageContent(data.emailForTestUser(), data.passwordForTestUser())));
		application.setPassword(data.passwordForTestUser());
		application.login(data.testUserName(), data.passwordForTestUser());
		verificator.verifyIfUserIsLogedIn();
		application.logout();
	}
	
	@Test
	public void createOffer() {
		application.loginDefaultUser();
		application.publishOfferWithoutImage("TestujemyDodawanie", "Oferta testowa", "TestowyProducent");
		verificator.verifyIfOfferExist("TestujemyDodawanie");
		application.logout();
	}
	
	@Test
	public void findOfferByCategoryTest() {
		application.findOfferByName(data.offerName());
		verificator.verifyIfOfferExist(data.offerName());
	}

	@Test
	public void findOfferByVendorTest() {
		application.findOfferByVendor(data.offerVendor());
		verificator.verifyIfOfferExist(data.offerName());
	}
	
	@Test
	public void findWishlistByUserTest() {
		application.findWishlistByUser(data.defaultUser());
		verificator.verifyIfWishlistExist(data.defaultWishlistToDefaultUser());
	}
	
	@Test
	public void findWishlistByEmailTest() {
		application.findWishlistByEmail(data.emailForDefaultUser());
		verificator.verifyIfWishlistExist(data.defaultWishlistToDefaultUser());
	}
	
	@Test
	public void loginTest() {
		application.loginDefaultUser();
		verificator.verifyIfUserIsLogedIn();
		application.logout();
	}

	@Test
	public void logoutTest() {
		application.loginDefaultUser();
		verificator.verifyIfUserIsLogedIn();
		application.logout();
		verificator.verifyIfUserIsLogedOut();
	}
	
	@Test
	public void createEmptyPublicWishlist() {
		application.loginDefaultUser();
		application.createWishlist("PublicznaTestowaPusta", "publicState");
		verificator.verifyIsWishlistVisibleForUser("PublicznaTestowaPusta");
		application.logout();
		verificator.verifyIsWishlistNoVisibleForUserAfterLogout("PublicznaTestowaPusta");
	}

	@Test
	public void createNoEmptyPublicWishlist() {
		application.loginDefaultUser();
		application.createWishlist("PublicznaTestowaPełna", "publicState");
		application.addWishToWishlistDiffrentFromDefault(data.nextOfferName(), "PublicznaTestowaPełna");
		application.logout();
		application.findWishlistByUser(data.defaultUser());
		verificator.verifyIsWishlistVisibleForUser("PublicznaTestowaPełna");
	}

	@Test
	public void createPrivateWishlist() {
		application.loginDefaultUser();
		application.createWishlist("PrywatnaTestowa", "privateState");
		verificator.verifyIsWishlistVisibleForUser("PrywatnaTestowa");
		application.logout();
		application.loginDefaultUser();
		application.findWishlistByUser(data.defaultUser());
		verificator.verifyIsWishlistVisibleForUser("PrywatnaTestowa");;
		application.logout();
		application.findWishlistByUser(data.defaultUser());
		verificator.verifyIsWishlistNoVisibleForUserAfterLogout("PrywatnaTestowa");
	}
	
	@Test
	public void createEmptySharedWishlist() {
		application.loginDefaultUser();
		application.createWishlist("DzielonaTestowa", "sharedState");
		verificator.verifyIsWishlistVisibleForUser("DzielonaTestowa");
		application.logout();
		verificator.verifyIsWishlistNoVisibleForUserAfterLogout("DzielonaTestowa");
	}
	
	@Test
	public void createNoEmptySharedWishlist() {
		application.loginDefaultUser();
		application.createWishlist("DzielonaTestowaPełna", "sharedState");
		application.addWishToWishlistDiffrentFromDefault(data.nextOfferName(), "DzielonaTestowaPełna");
		verificator.verifyIsWishlistVisibleForUser("DzielonaTestowaPełna");
		application.goToWishlistPage("DzielonaTestowaPełna");
		application.sendWishlistToFriend(data.emailForSecondUser());
		application.logout();
		application.login(data.friendForDefaultUser(), data.passwordForFriendOfDefaultUser());
		application.findWishlistByUser(data.defaultUser());
		verificator.verifyIsWishlistVisibleForUser("DzielonaTestowaPełna");;
		application.logout();
	}
	
	@Test
	public void addWishToWishlistForFirstTime() {
		application.loginDefaultUser();
		application.addWishToWishlistDiffrentFromDefault(data.offerName(), data.defaultWishlistToDefaultUser());
		application.goToWishlistPage(data.defaultWishlistToDefaultUser());
		verificator.verifyIfWishlistContainsWish(data.offerName());
		application.logout();
}
	
	@Test
	public void addWishToDefaultWishlistTest() {
		application.loginDefaultUser();
		application.addWishToWishlistDiffrentFromDefault(data.offerName(), data.defaultWishlistToDefaultUser());
		application.clickLink(baseUrl);
		application.addWishToDefaultWishlist(data.nextOfferName());
		application.goToWishlistPage(data.defaultWishlistToDefaultUser());
		verificator.verifyIfWishlistContainsWish(data.nextOfferName());
		application.logout();
	}

	@Test
	public void addWishToWishlistDiffrentFromDefaultTest() {
		application.loginDefaultUser();
		application.addWishToWishlistDiffrentFromDefault(data.offerName(), data.noDefaultWishlistName());
		application.goToWishlistPage(data.noDefaultWishlistName());
		verificator.verifyIfWishlistContainsWish(data.offerName());
		application.logout();
	}
	
	@Before
	public void clean() {
		application.prepare();
	}
	
	@BeforeClass
	public static void prepare() {
		application.prepare();
		registerFirstUser();
		registerSecondUser();
		publishOffers();
		createWishlists();
	    addWishlistToWishlist();
	}

	private static void addWishlistToWishlist() {
		application.clickLink(baseUrl);
 		application.addWishToWishlistDiffrentFromDefault(data.offerName(), data.defaultWishlistToDefaultUser());
 		application.logout();
	}

	private static void createWishlists() {
		application.createWishlist(data.publicWishlistName(), "publicState");
	    application.createWishlist(data.defaultWishlistToDefaultUser(), "publicState");
	    application.createWishlist(data.noDefaultWishlistName(), "publicState");
	}

	private static void publishOffers() {
		application.loginDefaultUser();
		application.publishOfferWithoutImage(data.offerName(), data.offerDescription(), data.offerVendor());
		application.publishOfferWithoutImage(data.nextOfferName(), data.offerDescription(), data.offerVendor());
	}

	private static void registerSecondUser() {
		application.register(data.secondUser(), data.firstnameSecondUser(), data.lastnameSecondUser(), data.emailForSecondUser());
		application.clickLink(inbox.extractLink(
							inbox.getLatestMessageContent(data.emailForSecondUser(), data.passwordForSecondUser())));
		application.setPassword(data.passwordForApplicationForSecondUser());
	}

	private static void registerFirstUser() {
		application.register(data.defaultUser(), data.firstnameDefaultUser(), data.lastnameDefaultUser(), data.emailForDefaultUser());
		application.clickLink(inbox.extractLink(
							inbox.getLatestMessageContent(data.emailForDefaultUser(), data.passwordForDefaultUser())));
		application.setPassword(data.passwordForApplicationForDefaultUser());
	}
	
	@AfterClass
	public static void closeBrowser() {
		driver.quit();
	}

}
