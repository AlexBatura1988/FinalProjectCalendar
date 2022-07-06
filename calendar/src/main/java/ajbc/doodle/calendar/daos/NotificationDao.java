package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface NotificationDao {
	
	// CRUD operations
		@Transactional(readOnly = false)
		public default void addNotification(Notification notification) throws DaoException {
			throw new DaoException("Method not implemented");
		}

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