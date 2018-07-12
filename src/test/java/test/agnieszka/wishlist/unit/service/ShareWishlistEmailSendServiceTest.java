package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.formatter.WishFormatter;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Offer;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;
import agnieszka.wishlist.model.WishlistState;
import agnieszka.wishlist.service.ShareWishlistEmailSendService;
import agnieszka.wishlist.service.WishlistMailService;

@RunWith(MockitoJUnitRunner.class)
public class ShareWishlistEmailSendServiceTest {

	@InjectMocks
	private ShareWishlistEmailSendService shareWishlistEmailSendService;
	
	@Mock private WishlistMailService wishlistMailService;
	@Mock private ApplicationMailer applicationMailer;
	@Mock private WishFormatter wishFormatter;
	@Mock private UrlHelper urlHelper;

	@Mock private WishlistMail wishlistMail;
	
	@Test
	public void testShareEmailFormatsAndSendsEmail() {
		//given
		Wishlist wishlist = aWishlist();
		EmailAddress emailAddress = new EmailAddress("test@test.com");
		String subject = "test mail subject";
		
		setField(shareWishlistEmailSendService, "subject", subject);
		setField(shareWishlistEmailSendService, "bodyTemplate", "test wishlist {0} {1} {2}");
		
		when(wishlistMailService.createWishlistMail(wishlist)).thenReturn(wishlistMail);
		when(urlHelper.createAbsoluteUrlForPath(anyString())).thenReturn(someLink());
		when(wishFormatter.formatWishlistForEmail(wishlist.getWishes())).thenReturn(someContent());
		
		when(wishlistMail.getMailId()).thenReturn(aMailId());
		
		ArgumentCaptor<String> captureMailBody = ArgumentCaptor.forClass(String.class);
		
		//when
		shareWishlistEmailSendService.shareWishlistViaEmail(wishlist, emailAddress);
		
		//then
		verify(wishFormatter, times(1)).formatWishlistForEmail(wishlist.getWishes());
		verify(applicationMailer, times(1)).sendMessage(eq(emailAddress), eq(subject), captureMailBody.capture());
		
		assertThat(captureMailBody.getValue())
				.contains(userName())
				.contains(someLink())
				.contains(someContent())
				.contains(aMailId());
	}

	private Wishlist aWishlist() {
		Wishlist wishlist = new Wishlist(wishlistName(), WishlistState.PUBLIC);
		
		Offer offer = new Offer();
		offer.setName(offerName());
		
		wishlist.add(offer);
		
		User user = new User();
		user.setUserId(userName());
		
		wishlist.setWisher(user);
		
		return wishlist;
	}

	private String wishlistName() {
		return "testWishlist";
	}

	private String offerName() {
		return "testOffer";
	}

	private String userName() {
		return "testUser";
	}

	private String someLink() {
		return "test/path";
	}

	private String someContent() {
		return "formattedContent";
	}

	private String aMailId() {
		return "15";
	}

}
