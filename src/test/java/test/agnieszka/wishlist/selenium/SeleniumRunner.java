package test.agnieszka.wishlist.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import test.agnieszka.wishlist.ui.SeleniumRunnerData;

public class SeleniumRunner {

	private static String baseUrl = "http://localhost:8080/wishlist/offers";
	private WebDriver driver;

	private final SeleniumRunnerData data = new SeleniumRunnerData();
	
	public SeleniumRunner(WebDriver driver) {
		this.driver = driver;
		this.driver.get(baseUrl);
	}


	public void publishOfferWithoutImage(String name, String descritption, String vendor) {
		driver.findElement(By.linkText("Dodaj ofertę")).click();
		driver.findElement(By.id("name")).sendKeys(name);
		driver.findElement(By.id("description")).sendKeys(descritption);
		driver.findElement(By.id("vendor")).sendKeys(vendor);
		driver.findElement(By.id("addOffer")).click();
	}
	
	public void register(String userId, String firstname, String lastname, String email) {
		driver.findElement(By.id("register")).click();

		driver.findElement(By.id("userId")).sendKeys(userId);
		driver.findElement(By.id("firstname")).sendKeys(firstname);
		driver.findElement(By.id("lastname")).sendKeys(lastname);
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("registerSubmit")).click();
	}
	
	public void clickLink(String link) {
		driver.get(link);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	public void setPassword(String password) {
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("passwordConfirmation")).sendKeys(password);
		driver.findElement(By.id("setPassword")).click();
	}

	public void login(String name, String password) {
		driver.findElement(By.id("login")).click();
		
		driver.findElement(By.name("j_username")).sendKeys(name);
		driver.findElement(By.name("j_password")).sendKeys(password);
		driver.findElement(By.name("j_submit")).click();
	}
	
	public void loginDefaultUser() {
		login(data.defaultUser(), data.passwordForApplicationForDefaultUser());
	}
	
	public void loginSecondUser() {
		login(data.secondUser(), data.passwordForApplicationForSecondUser());
	}
	
	public void logout() {
		driver.findElement(By.id("logout")).click();
	}
	
	public void findOfferByName(String name) {
		driver.findElement(By.linkText("Wyszukaj ofertę")).click();
		driver.findElement(By.id("searchFor")).sendKeys(name);
		
		if (driver.findElement(By.id("optionVendor")).isSelected()) {
			driver.findElement(By.id("optionName")).click();
		}
		driver.findElement(By.id("btnSearch")).click();
	}

	public void findOfferByVendor(String keyword) {
		driver.findElement(By.linkText("Wyszukaj ofertę")).click();
		driver.findElement(By.id("searchFor")).sendKeys(keyword);
		
		if (driver.findElement(By.id("optionName")).isSelected()) {
			driver.findElement(By.id("optionVendor")).click();
		}
		driver.findElement(By.id("btnSearch")).click();
	}
	
	public void findWishlistByUser(String username) {
		driver.findElement(By.linkText("Wyszukaj listę życzeń")).click();
		driver.findElement(By.id("term")).sendKeys(username);
		
		if (driver.findElement(By.id("optionEmail")).isSelected()) {
			driver.findElement(By.id("optionUser")).click();
		}
		driver.findElement(By.id("btnSearch")).click();
	}
	
	public void findWishlistByEmail(String email) {
		driver.findElement(By.linkText("Wyszukaj listę życzeń")).click();
		driver.findElement(By.id("term")).sendKeys(email);
		
		if (driver.findElement(By.id("optionUser")).isSelected()) {
			driver.findElement(By.id("optionEmail")).click();
		}
		driver.findElement(By.id("btnSearch")).click();
	}
	
	public void createWishlist(String name, String category) {
		driver.findElement(By.linkText("Stwórz listę życzeń")).click();
		driver.findElement(By.id("name")).sendKeys(name);
		driver.findElement(By.id(category)).click();
		driver.findElement(By.id("btnCreateWishlist")).click();
	}
	
	public void addWishToDefaultWishlist(String offer) {
		driver.findElement(By.linkText(offer)).click();
		driver.findElement(By.linkText("Dodaj do aktualnej listy")).click();
	}
	
	public void addOfferToWishlistDiffrentFromDefault(String offer, String wishlist) {
		driver.get(baseUrl);
		driver.findElement(By.linkText(offer)).click();
		driver.findElement(By.id("wishlistChoiceButton")).click();
		driver.findElement(By.id(wishlist)).click();
		driver.findElement(By.id("submitButton")).click();
	}
	
	public void goToWishlistPage(String wishlist) {
		driver.findElement(By.linkText(wishlist)).click();
	}
	
	public void prepare(){
		driver.get(baseUrl);
		if (driver.findElements(By.id("logout")).size() != 0) {
			driver.findElement(By.id("logout")).click();
		}
	}

	public void sendWishlistToFriend(String emailForSecondUser) {
		driver.findElement(By.id("email")).sendKeys(emailForSecondUser);
		driver.findElement(By.id("submitSendList")).click();
	}

}
