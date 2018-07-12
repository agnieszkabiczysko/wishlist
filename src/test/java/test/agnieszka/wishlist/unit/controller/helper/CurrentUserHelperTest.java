package test.agnieszka.wishlist.unit.controller.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserHelperTest {
	
	@InjectMocks
	private CurrentUserHelper helper;
	
	@Mock
	private UserService userService;

	@Test
	public void returnNullForNullPrincipal() {
		//when
		User currentUser = helper.getCurrentUser(null);
		
		//then
		assertThat(currentUser).isNull();
	}
	
	@Test
	public void returnUserForNonNullPrincipal() {
		//given
		Principal principal = mock(Principal.class);
		User user = new User();
		
		when(userService.getCurrentUser()).thenReturn(user);
		
		//when
		User currentUser = helper.getCurrentUser(principal);
		
		//then
		assertThat(currentUser).isEqualTo(user);
	}

}
