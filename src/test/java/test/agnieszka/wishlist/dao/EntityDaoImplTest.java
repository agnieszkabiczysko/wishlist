package test.agnieszka.wishlist.dao;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.agnieszka.wishlist.configuration.HibernateTestConfiguration;

@ContextConfiguration(classes = {HibernateTestConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class EntityDaoImplTest {

	@Autowired
	DataSource dataSource;

}
