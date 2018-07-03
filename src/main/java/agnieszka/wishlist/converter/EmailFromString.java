package agnieszka.wishlist.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import agnieszka.wishlist.model.EmailAddress;

@Component
public class EmailFromString implements Converter<String, EmailAddress> {

	@Override
	public EmailAddress convert(String email) {
		return new EmailAddress(email);
	}

}
