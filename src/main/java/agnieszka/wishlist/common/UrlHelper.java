package agnieszka.wishlist.common;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Component
public class UrlHelper {

	public String createRegisterUrl(HttpServletRequest request) {
		UriComponents ucb = ServletUriComponentsBuilder
				.fromContextPath(request)
				.path("/register/")
				.build();
		URI uri = ucb.toUri();
		return uri.toString();
	}
	
	public String createWishlistUrl(HttpServletRequest request) {
		UriComponents ucb = ServletUriComponentsBuilder
				.fromContextPath(request)
				.path("/wishlist/")
				.build();
		URI uri = ucb.toUri();
		return uri.toString();
	}
}
