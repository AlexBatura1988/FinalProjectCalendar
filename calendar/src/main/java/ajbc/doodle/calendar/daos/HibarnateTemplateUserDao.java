package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Repository("htUsDao")
public class HibarnateTemplateUserDao implements UserDao {
	
	

	@Autowired
	private HibernateTemplate template;

	@Override
	public void createUser(User user) throws DaoException {
		// open session /connection to db
		template.persist(user);
		// close session
	}
	
	@Override
    public User getUser(Integer userId) throws DaoException {
        User user = template.get(User.class, userId);
        if (user == null) {
            throw new DaoException("No Such User in DB");
        }
        return user;
    }
	
	 @Override
	    public User getUser(String email) throws DaoException {
	        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
	        criteria.add(Restrictions.eq("email", email));
	        List<User> users = (List<User>) template.findByCriteria(criteria);
	        if (users.size() == 0) {
	            throw new DaoException("No Such User in DB");
	        }
	        return users.get(0);
	    }
	
	

	@Override
	public void updateUser(User user) throws DaoException {
		template.merge(user);
	}
	
	 @Override
	    public void softDeleteUser(Integer userId) throws DaoException {
	        User user = getUser(userId);
	        user.setDisable(1);
	        updateUser(user);
	    }
	 
	 @Override
	    public void hardDeleteUser(Integer userId) throws DaoException {
	        User user = getUser(userId);
	        template.delete(user);
	    }
	 
	 public List<User> getAllUsers() throws DaoException {
	        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
	        return (List<User>) template.findByCriteria(criteria);
	    }
	 
	 @Override
	    public List<User> getUsersByEventId(Integer eventId) throws DaoException {
	        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).createCriteria("events");
	        criteria.add(Restrictions.eq("id", eventId));
	        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        return (List<User>) template.findByCriteria(criteria);
	    }
	 
	 @Override
	    public List<User> getUsersByEventBetweenDates(LocalDateTime stateDate, LocalDateTime endDate) throws DaoException {
	        DetachedCriteria criteria = DetachedCriteria.forClass(User.class).createCriteria("events");
	        criteria.add(Restrictions.le("startDate", stateDate));
	        criteria.add(Restrictions.ge("endDate", endDate));
	        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        return (List<User>) template.findByCriteria(criteria);
	    }
	 
	 @Override
		public List<User> getDiscontinuedUsers() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
			criteria.add(Restrictions.eq("disable", 1));
			return (List<User>)template.findByCriteria(criteria);
		}

	

	 @Override
	    public void hardDeleteAllUsers() throws DaoException {

	        template.deleteAll(getAllUsers());
	    }


}
