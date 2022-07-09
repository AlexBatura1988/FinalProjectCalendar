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
	public void addNotification(Notification notification) throws DaoException, NotAuthorizedException {
		if (!notification.getEvent().getOwnerId().equals(notification.getOwner().getUserId())) {
			throw new NotAuthorizedException("Only the owner of an event can add notifications");
		}
		notificationDao.addNotification(notification);
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
