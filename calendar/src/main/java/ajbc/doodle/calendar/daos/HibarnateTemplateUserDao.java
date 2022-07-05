package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Component(value="htDao")
public class HibarnateTemplateUserDao implements UserDao {
	
	@Autowired
	private HibernateTemplate template;

	@Override
	public void addUser(User user) throws DaoException {
		// open session /connection to db
		template.persist(user);
		// close session
	}

	@Override
	public void updateUser(User user) throws DaoException {
		template.merge(user);
	}

	@Override
	public User getProduct(Integer userId) throws DaoException {
		User user = template.get(User.class, userId);
		if (user ==null)
			throw new DaoException("No Such User in DB");
		return user;
	}

	@Override
	public void deleteUser(Integer userId) throws DaoException {
		User user = getProduct(userId);
		user.setDisable(1);;
		updateUser(user);
	}

	@Override
	public List<User> getAllUsers() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		return (List<User>)template.findByCriteria(criteria);
	}

	@Override
	public List<User> getDiscontinuedUsers() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("disable", 1));
		return (List<User>)template.findByCriteria(criteria);
	}

}
