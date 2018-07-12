package test.agnieszka.wishlist.unit.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import agnieszka.wishlist.model.Password;
import agnieszka.wishlist.service.PasswordServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceImplTest {
	
	@InjectMocks
	private PasswordServiceImpl passwordService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void creatingPasswordShouldInvokeEncoder() {
		//given
		String plainText = "test";
		Password password = aPassword(plainText);
		
		//when
		passwordService.createPasswordHash(password);
		
		//then
		verify(passwordEncoder, times(1)).encode(plainText);
	}
	
	private Password aPassword(String plainText) {
		Password password = new Password();
		password.setPassword(plainText);
		return password;
	}
}
