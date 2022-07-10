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
	 * @param notification - the notification to create
	 *  Add notification to the DB
	 */
		@Transactional(readOnly = false)
		public default void addNotification(Notification notification) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		/**
	     * Update notification in the DB
	     * @param notification = the modified notification
	     */
		@Transactional(readOnly = false)
		public default void updateNotifivation(Notification notification) throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default Notification getNotifivationById(Integer notificationId) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		public default List<Notification> getAllNotifications() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		@Transactional(readOnly = false)
		public default void deleteNotificationById(Integer notificationId) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		@Transactional(readOnly = false)
		public default void hardDeleteNotificationById() throws  DaoException {
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

}
