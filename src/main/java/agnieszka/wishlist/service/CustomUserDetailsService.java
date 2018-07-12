package agnieszka.wishlist.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserState;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	@Value("${user.notFound}")
	private String userNotFoundMessage;

	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userService.findUserByUserId(userId);
		
		if (user == null) {
			throw new UsernameNotFoundException(userNotFoundMessage);
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getUserId(),
				user.getPassword(), 
                user.getState() == UserState.ACTIVE,
                true,
                true,
                true,
                getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		return user.getUserProfiles()
				.stream()
				.map(profile -> new SimpleGrantedAuthority("ROLE_" + profile.getType()))
				.collect(toList());
	}

}
