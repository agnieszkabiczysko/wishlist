package test.agnieszka.wishlist.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;

import agnieszka.wishlist.controller.FriendsController;
import agnieszka.wishlist.controller.helper.CurrentUserHelper;
import agnieszka.wishlist.model.User;

public class FriendControllerTest {

	@Mock
	private CurrentUserHelper currentUserHelper;
	
	@Spy
	private ModelMap model;
	
	@Mock
	private Principal principal;
	
	@InjectMocks
	private FriendsController controller;
	
	private User user;
	private Set<User> friends = new HashSet<>();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		friends = getFriends();
		user = getUser();
	}
	
	@Test
	public void friendsList() {
		//given
		when(principal.getName()).thenReturn(user.getUserId());
		when(currentUserHelper.getCurrentUser(principal)).thenReturn(user);
		
		//when
		String viewName = controller.friendsList(model, principal);
		
		//then
		assertEquals("friends", viewName);
		assertEquals(friends, model.get("friends"));
	}
	
	private User getUser() {
		User user = new User();
		user.setUserId("U1");
		user.setFriends(friends);
		
		return user;
	}
	
	private Set<User> getFriends() {
		User friend = new User();
		friend.setUserId("U2");
		
		Set<User> friends = new HashSet<>();
		friends.add(friend);
		
		return friends;
	}
}
