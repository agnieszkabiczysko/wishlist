package test.agnieszka.wishlist.unit.controller;

import static org.junit.Assert.assertEquals;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.SecurityController;

public class SecurityControllerTest {

	@Mock
	private Principal principal;
	
	@Spy
	private ModelMap model;
	
	@InjectMocks
	private SecurityController controller;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void login() {
		//when
		String viewName = controller.login(model);
		
		//then
		assertEquals("login", viewName);
	}
	
	@Test
	public void loginError() {
		//when
		String viewName = controller.loginError(model);
		
		//then
		assertEquals("login", viewName);
		assertEquals("true", model.get("error"));
	}
	
	@Test
	public void logout() {
		//when
		String viewName = controller.logout(model);
		
		//then
		assertEquals("login", viewName);
		assertEquals(true, model.get("logout"));
	}
}
