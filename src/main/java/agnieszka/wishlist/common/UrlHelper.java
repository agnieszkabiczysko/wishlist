package agnieszka.wishlist.common;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Component
public class UrlHelper {

	public String createAbsoluteUrlForPath(String path) {
		UriComponents uriComponents = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path(path)
				.build();
		URI uri = uriComponents.toUri();
		return uri.toString();
	}
	
}
