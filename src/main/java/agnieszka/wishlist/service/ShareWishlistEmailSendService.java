package agnieszka.wishlist.service;

import static java.text.MessageFormat.format;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import agnieszka.wishlist.common.ApplicationMailer;
import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.formatter.WishFormatter;
import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.Wish;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.model.WishlistMail;

@Service
public class ShareWishlistEmailSendService {

	@Value("${wishlistMail.subject}")
	private String subject;
	
	@Value("${wishlistMail.bodyTemplate}")
	private String bodyTemplate;
			
	@Autowired
	private WishlistMailService mailService;
	
	@Autowired
	private ApplicationMailer mailer;

	@Autowired
	private WishFormatter formatter;

	@Autowired
	private UrlHelper urlHelper;

	public void shareWishlistViaEmail(Wishlist wishlist, EmailAddress emailAddress) {
		WishlistMail wishlistMail = recordEmailFor(wishlist);
		
		String body = prepareBodyFor(wishlist, wishlistMail);
		
		mailer.sendMessage(emailAddress, subject, body);
	}

	private WishlistMail recordEmailFor(Wishlist wishlist) {
		return mailService.createWishlistMail(wishlist);
	}

	private String prepareBodyFor(Wishlist wishlist, WishlistMail wishlistMail) {
		String wisherId = wishlist.getWisher().getUserId();
		String formattedWishes = formatWishes(wishlist.getWishes());
		String wishlistUrl = wishlistUrl(wishlistMail);
		
		return format(bodyTemplate, wisherId, formattedWishes, wishlistUrl);
	}

	private String formatWishes(Set<Wish> wishes) {
		return formatter.formatWishlistForEmail(wishes);
	}

	private String wishlistUrl(WishlistMail wishlistMail) {
		return urlHelper.createAbsoluteUrlForPath("/wishlist") + "?mailId=" + wishlistMail.getMailId();
	}

}
