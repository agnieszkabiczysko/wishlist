package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.model.ConfirmationMail;
import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.service.ConfirmationMailServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationMailServiceImplTest {

	@InjectMocks
	private ConfirmationMailServiceImpl confirmationMailService;
	
	@Mock
	private RegisterMail mailingInstance;
	
	@Mock
	private UrlHelper urlHelper;
	
	@Test
	public void testConfirmationMailGeneration() {
		//given
		when(mailingInstance.getConfirmationId()).thenReturn("12345");
		
		when(urlHelper.createAbsoluteUrlForPath(anyString())).thenReturn("test/path");
		
		setField(confirmationMailService, "title", "someTitle");
		setField(confirmationMailService, "bodyTemplate", "mail body {0} test");
		
		//when
		ConfirmationMail confirmationMail = confirmationMailService.prepareMail(mailingInstance);
		
		//then
		assertThat(confirmationMail.getTitle()).isNotNull();
		assertThat(confirmationMail.getBody()).contains("test/path/12345");
		verify(mailingInstance, times(1)).getConfirmationId();
	}

}
