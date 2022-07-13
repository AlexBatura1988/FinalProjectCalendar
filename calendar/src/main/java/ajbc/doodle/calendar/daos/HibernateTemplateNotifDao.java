package ajbc.doodle.calendar.daos;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import ajbc.doodle.calendar.entities.Notification;

@SuppressWarnings("unchecked")
@Repository("htNotDao")
public class HibernateTemplateNotifDao implements NotificationDao {

	@Autowired
	private HibernateTemplate template;

	@Override
	public void addNotification(Notification notification) throws DaoException {
		template.persist(notification);

	}

	@Override
	public Notification updateNotification(Notification notification) throws DaoException {
		return template.merge(notification);
	}

	@Override
	public Notification getNotificationById(Integer notificationId) throws DaoException {
		Notification notification = template.get(Notification.class, notificationId);
		if (notification == null)
			throw new DaoException("No notification   in the DB with id: " + notificationId);
		return notification;
	}

	@Override
	public List<Notification> getNotificationsByIds(List<Integer> notificationIds) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.in("id", notificationIds));
		return (List<Notification>) template.findByCriteria(criteria);
	}

	@Override
	public List<Notification> getAllNotifications() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Notification>) template.findByCriteria(criteria);
	}

	@Override
	public void deleteNotification(Notification notification) throws DaoException {
		
		template.delete(notification);
	}
	
	 @Override
	    public void deleteNotifications(Set<Notification> notifications) throws DaoException {
	        template.deleteAll(notifications);
	    }

//	@Override
//	public void hardDeleteNotificationById() throws DaoException {
//		// TODO Auto-generated method stub
//		NotificationDao.super.hardDeleteNotificationById();
//	}
//
//	@Override
//	public void deleteAllNotifications() throws DaoException {
//		// TODO Auto-generated method stub
//		NotificationDao.super.deleteAllNotifications();
//	}

	@Override
	public long count() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.setProjection(Projections.rowCount());
		return (long) template.findByCriteria(criteria).get(0);
	}

	@Override
	public List<Notification> getDisableNotification() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.eq("disable", 1));
		return (List<Notification>) template.findByCriteria(criteria);
	}
	
//	@Override
//	public List<Notification> getNotificationsByEventId(Integer eventId) throws DaoException {
//		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
//		criteria.add(Restrictions.eq("disable", 1));
//		return (List<Notification>) template.findByCriteria(criteria);
//	}

}
