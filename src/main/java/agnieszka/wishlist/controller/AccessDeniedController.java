package agnieszka.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {

	private static final String ACCESS_DENIED_VIEW = "accessDenied";

	@RequestMapping(value = "/accessDenied")
	public String accessDenied() {
		return ACCESS_DENIED_VIEW;
	}
			
}
