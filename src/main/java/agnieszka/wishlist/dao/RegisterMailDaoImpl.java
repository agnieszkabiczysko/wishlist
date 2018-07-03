package agnieszka.wishlist.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import agnieszka.wishlist.model.RegisterMail;
import agnieszka.wishlist.model.User;

@Repository("registerMailDao")
public class RegisterMailDaoImpl extends AbstractDao<String, RegisterMail> implements RegisterMailDao {

	@Override
	public void saveRegistrationMail(RegisterMail registerMail) {
		persist(registerMail);
	}

	@Override
	public RegisterMail findMailByMailingId(String uuid) {
		return getByKey(uuid);
	}

	@Override
	public RegisterMail findMailByConfirmationId(String uuid) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("confirmationId", uuid));
		return (RegisterMail) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegisterMail> findMailsForUser(User user) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("user", user));
		return (List<RegisterMail>) criteria.list();
	}

	@Override
	public RegisterMail findActiveMailForUser(User user) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("user", user));
		return (RegisterMail) criteria.addOrder(Order.desc("mailingTime")).list().get(0);
	}
}
