package agnieszka.wishlist.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.User;
import agnieszka.wishlist.model.UserPreferences;

@Repository("userPreferencesDao")
public class UserPreferencesDaoImpl extends AbstractDao<Integer, UserPreferences> implements UserPreferencesDao {

	@Override
	public void saveUserPreferences(UserPreferences preferences) {
		persist(preferences);
	}

	@Override
	public UserPreferences findUserPreferencesForUser(User user) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("userId", user.getId()));
		return (UserPreferences) criteria.uniqueResult();
	}

}
