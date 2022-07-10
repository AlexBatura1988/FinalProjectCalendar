package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.exceptions.NotAuthorizedException;

/**
 * Notification service
 */
@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNotDao")
	private NotificationDao notificationDao;

	/**
	 * Create notification
	 * 
	 * @param notification - the notification to create
	 */
	public void addNotification(Notification notification) throws DaoException {

		notificationDao.addNotification(notification);
	}

	/**
	 * 
	 * Create notification
	 * 
	 * @param owner        - the owner of the event * @param event - the event to of
	 *                     the notification
	 * @param notification - the notification to create
	 * 
	 *                     public List<Notification> getAllNotification() throws
	 *                     DaoException { return
	 *                     notificationDao.getAllNotifications(); }
	 * @throws NotAuthorizedException
	 */

	public void addNotification(User owner, Event event, Notification notification)
			throws DaoException {
		notification.setOwner(owner);

		notification.setEvent(event);
		this.addNotification(notification);

	}

	public List<Notification> getAllNotification() throws DaoException {
		return notificationDao.getAllNotifications();

	}

	public Notification getNotificationById(Integer notificationId) throws DaoException {
		return notificationDao.getNotifivationById(notificationId);
	}

	public void updateNotification(Notification notif) throws DaoException {
		notificationDao.updateNotifivation(notif);
	}

	public void deleteNotificationById(Integer notificationId) throws DaoException {
		notificationDao.deleteNotificationById(notificationId);
	}

}
