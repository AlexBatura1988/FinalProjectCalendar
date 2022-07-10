package ajbc.doodle.calendar.daos;

import java.util.List;

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
	public void updateNotifivation(Notification notification) throws DaoException {
		template.merge(notification);
	}

	@Override
	public Notification getNotifivationById(Integer notificationId) throws DaoException {
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
		return (List<Notification>) template.findByCriteria(criteria);
	}

	@Override
	public void deleteNotificationById(Integer notificationId) throws DaoException {
		Notification notif = getNotifivationById(notificationId);
		notif.setDisable(1);;
		updateNotifivation(notif);
	}

	@Override
	public void hardDeleteNotificationById() throws DaoException {
		// TODO Auto-generated method stub
		NotificationDao.super.hardDeleteNotificationById();
	}

	@Override
	public void deleteAllNotifications() throws DaoException {
		// TODO Auto-generated method stub
		NotificationDao.super.deleteAllNotifications();
	}

	@Override
	public long count() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.setProjection(Projections.rowCount());
		return (long)template.findByCriteria(criteria).get(0);
	}

	@Override
	public List<Notification> getDisableNotification() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.eq("disable", 1));
		return (List<Notification>)template.findByCriteria(criteria);
	}

}
