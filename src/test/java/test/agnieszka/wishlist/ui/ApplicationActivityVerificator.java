package test.agnieszka.wishlist.ui;

import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ApplicationActivityVerificator {
	
	private final WebDriver driver;

	public ApplicationActivityVerificator(WebDriver driver) {
		this.driver = driver;
	}

	public void verifyIfOfferExist(String offerName) {
		assertNotNull(driver.findElement(By.linkText(offerName)));
	}

	public void verifyIfWishlistExist(String wishlistName) {
		assertNotNull(driver.findElement(By.linkText(wishlistName)));
	}

	public void verifyIsWishlistVisibleForUser(String name) {
		assertNotNull(driver.findElement(By.linkText(name)));
	}

	public void verifyIsWishlistNoVisibleForUserAfterLogout(String name) {
		assertNotNull(driver.findElements(By.linkText(name)));
	}

	public void verifyIfUserIsLogedOut() {
		assertNotNull(driver.findElement(By.id("login")));
	}

	public void verifyIfUserIsLogedIn() {
		assertNotNull(driver.findElement(By.linkText("Dodaj ofertę")));
		assertNotNull(driver.findElement(By.id("logout")));
	}

	public void verifyIfWishlistContainsWish(String offer) {
		assertNotNull(driver.findElement(By.linkText(offer)));
	}

}
