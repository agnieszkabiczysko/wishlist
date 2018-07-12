package agnieszka.wishlist.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<PK extends Serializable, T> {

	public final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public T getByKey(PK key) {
		return (T) (getSession().get(persistentClass, key));
	}
	
	public void persist(T entity) {
		getSession().persist(entity);
	}
	
	public void merge(T entity) {
		getSession().merge(entity);
	}
	
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	public void update(T entity) {
		getSession().update(entity);
	}
	
	@SuppressWarnings("deprecation")
	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(persistentClass);
	}
}
