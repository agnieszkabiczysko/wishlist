package test.agnieszka.wishlist.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import agnieszka.wishlist.dao.UserPreferencesDao;
import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;
import agnieszka.wishlist.model.Wishlist;
import agnieszka.wishlist.service.UserPreferencesServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserPreferencesServiceImplTest {

	@Mock
	private User user;
	
	@Mock
	private Wishlist wishlist;
	
	@Mock
	private UserPreferencesDao dao;
	
	@InjectMocks
	private UserPreferencesServiceImpl preferencesService;
	
	@Spy
	private UserPreferences userPreferences;
	
	@Before
	public void setup() {
		userPreferences = getUserPreferences();
	}
	
	@Test
	public void saveUserPreferences() {
		//given
		doNothing().when(dao).saveUserPreferences(any(UserPreferences.class));
		
		//when
		preferencesService.saveUserPreferences(any(UserPreferences.class));
		
		//when
		verify(dao, times(1)).saveUserPreferences(any(UserPreferences.class));
	}
	
	@Test
	public void findUserPreferencesForUser() {
		//given
		when(dao.findUserPreferencesForUser(any(User.class))).thenReturn(userPreferences);
		
		//when
		UserPreferences preferencesFound = preferencesService.findUserPreferencesForUser(any(User.class));
		
		//then
		verify(dao, times(1)).findUserPreferencesForUser(any(User.class));
		assertThat(preferencesFound).isEqualTo(userPreferences);
	}
	
	@Test
	public void saveUserPreferencesForUserWhenUserPreferencesDoesNotExist() {
		//given
		when(dao.findUserPreferencesForUser(any(User.class))).thenReturn(null);
		doNothing().when(dao).saveUserPreferences(any(UserPreferences.class));
		
		//when
		preferencesService.updateCurrentWishlist(user, wishlist);
		
		//then
		verify(dao, times(1)).saveUserPreferences(any(UserPreferences.class));
	}
	
	@Test
	public void saveUserPreferencesForUserWhenUserPreferencesExist() {
		//given
		when(dao.findUserPreferencesForUser(any(User.class))).thenReturn(userPreferences);
		
		//when
		preferencesService.updateCurrentWishlist(user, wishlist);
		
		//then
		verify(dao, times(1)).saveUserPreferences(any(UserPreferences.class));
	}
	
	@Test
	public void hasUserPreferencesForUser() {
		//given
		when(dao.findUserPreferencesForUser(any(User.class))).thenReturn(userPreferences);
		
		//when
		Boolean hasUserPreferencesForUser = preferencesService.userHasCurrentWishlist(user);
		
		//then
		assertTrue(hasUserPreferencesForUser);
	}

	
	@Test
	public void hasNotUserPreferencesForUser() {
		//given
		when(dao.findUserPreferencesForUser(any(User.class))).thenReturn(null);
		
		//when
		Boolean hasUserPreferencesForUser = preferencesService.userHasCurrentWishlist(user);
		
		//then
		assertFalse(hasUserPreferencesForUser);
	}
	
	
	private UserPreferences getUserPreferences() {
		return new UserPreferences(user, wishlist);
	}
}
