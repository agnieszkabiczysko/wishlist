package agnieszka.wishlist.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.EmailAddress;
import agnieszka.wishlist.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

	@Override
	public void save(User user) {
		persist(user);
	}

	
	@Override
	public void merge(User user) {
		super.merge(user);
	}
	
	public void update(User user) {
		super.update(user);
	}

	@Override
	public User findUserById(int id) {
		return getByKey(id);
	}

	@Override
	public User findUserByUserId(String userId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("userId", userId));
		return (User) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		Criteria criteria = createEntityCriteria();
		return (List<User>) criteria.list();
	}

	@Override
	public User findUserByEmail(EmailAddress email) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("email", email));
		return (User) criteria.uniqueResult();
	}
}
