package agnieszka.wishlist.service;

import static java.text.MessageFormat.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import agnieszka.wishlist.common.UrlHelper;
import agnieszka.wishlist.model.ConfirmationMail;
import agnieszka.wishlist.model.RegisterMail;

@Service
public class ConfirmationMailServiceImpl implements ConfirmationMailService {
	@Value("${registrationEmail.title}")
	private String title;
	
	@Value("${registrationEmail.bodyTemplate}")
	private String bodyTemplate;
	
	@Autowired
	private UrlHelper urlHelper;

	@Override
	public ConfirmationMail prepareMail(RegisterMail mailingInstance) {
		return new ConfirmationMail(title, formatBodyUsing(mailingInstance));
	}

	private String formatBodyUsing(RegisterMail mailingInstance) {
		return format(bodyTemplate, confirmationUrl(mailingInstance.getConfirmationId()));
	}

	private String confirmationUrl(String confirmationId) {
		return urlHelper.createAbsoluteUrlForPath("/confirm") + "/" + confirmationId;
	}

}
