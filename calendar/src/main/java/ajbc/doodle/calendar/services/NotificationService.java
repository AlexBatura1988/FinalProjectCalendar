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
import ajbc.doodle.calendar.exceptions.ForbiddenException;
import ajbc.doodle.calendar.exceptions.NotAuthorizedException;

/**
 * Notification service
 */
@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNotDao")
	private NotificationDao notificationDao;
	public static final int NOTIFICATIONS_LIMIT = 10;

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
	 * @param owner        - the owner of the event
	 * @param event        - the event to of the notification
	 * @param notification - the notification to create
	 */

	public void addNotification(User owner, Event event, Notification notification)
			throws DaoException, ForbiddenException {

		if ((long) event.getNotifications().size() >= NOTIFICATIONS_LIMIT) {
			throw new ForbiddenException("The owner can add only " + NOTIFICATIONS_LIMIT + " notifications per event");
		}

		notification.setOwner(owner);

		notification.setEvent(event);
		this.addNotification(notification);

	}

	/**
	 * Get all notification
	 * 
	 * @return list of notifications
	 */

	public List<Notification> getAllNotification() throws DaoException {
		return notificationDao.getAllNotifications();

	}

	/**
	 * Get the notification by id
	 * 
	 * @param notificationId - the notification id
	 * @return a notification
	 */

	public Notification getNotificationById(Integer notificationId) throws DaoException {
		return notificationDao.getNotificationById(notificationId);
	}

	/**
	 * Get the notifications of notificationIds
	 * 
	 * @param notificationIds - the ids to extract
	 * @return list of notifications
	 */
	public List<Notification> getNotificationsByIds(List<Integer> notificationIds) throws DaoException {
		return notificationDao.getNotificationsByIds(notificationIds);
	}

	/**
	 * update an existing notification
	 * 
	 * @param notification - the modified notification
	 */
	public Notification  updateNotification(Notification notification) throws DaoException {
		Notification original = this.getNotificationById(notification.getNotificationId());
		original.merge(notification);
		return notificationDao.updateNotification(original);
	}

	/**
	 * Delete the notification
	 * 
	 * @param notification - the notification to delete
	 * @param soft         - if soft delete or hard delete
	 */
	public Notification  deleteNotification(Notification notification, Boolean soft) throws DaoException {
		if (soft) {
			notification.setDisable(1);
			return notificationDao.updateNotification(notification);
		} else {
			// TODO
		}
		return null;
	}
	
//	public List<Notification> getNotificationsByEventId(Integer eventId) throws DaoException {
//		return notificationDao.getNotificationsByEventId(eventId);
//	}

}
