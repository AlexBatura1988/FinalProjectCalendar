package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;

/**
 * Notification DAO
 */
@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface NotificationDao {

	// CRUD operations

	/**
	 * 
	 * @param notification - the notification to create Add notification to the DB
	 */
	@Transactional(readOnly = false)
	public default void addNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	/**
	 * Update notification in the DB
	 * 
	 * @param notification = the modified notification
	 * @return 
	 */
	@Transactional(readOnly = false)
	public default Notification updateNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	/**
	 * Get notification by id
	 * 
	 * @param notificationId - the id of the notification
	 * @return a notification object
	 */
	public default Notification getNotificationById(Integer notificationId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	/**
     * Get notifications from notificationIds
     * @param notificationIds - the ids of the wanted notifications
     * @return list of notifications
     */
    public default List<Notification> getNotificationsByIds(List<Integer> notificationIds) throws DaoException {
        throw new DaoException("Method not implemented");
    }

	
	
    /**
     * Get all notifications
     * @return the list of every notification
=     */
	public default List<Notification> getAllNotifications() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void deleteNotificationById(Integer notificationId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void hardDeleteNotificationById() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void deleteAllNotifications() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default long count() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default List<Notification> getDisableNotification() throws DaoException {
		throw new DaoException("Method not implemented");
	}
//	
//	public default List<Notification> getNotificationsByEventId(Integer eventId) throws DaoException {
//		throw new DaoException("Method not implemented");
//	}

}
