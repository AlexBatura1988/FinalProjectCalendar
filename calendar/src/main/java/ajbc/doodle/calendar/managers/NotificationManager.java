package ajbc.doodle.calendar.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.Application;
import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import ajbc.doodle.calendar.threads.NotificationWorker;
import lombok.Setter;

@Setter
@Component
public class NotificationManager {

	private static final int QUEUE_INITIAL_CAPACITY = 11;
	private static final int NUM_OF_THREAD = 1;

	private ThreadGroup workersGroup;

	private PriorityBlockingQueue<Notification> notificationsQueue = new PriorityBlockingQueue<>(QUEUE_INITIAL_CAPACITY,
			new Comparator<Notification>() {
				@Override
				public int compare(Notification n1, Notification n2) {
					return n1.getNotificationDateTime().compareTo(n2.getNotificationDateTime());
				}
			});

	private final WebPushManager webPushManager;

	private final NotificationService notificationService;

	private final UserService userService;

	private final EventService eventService;

	public NotificationManager(NotificationService notificationService, WebPushManager webPushManager,
			UserService userService, EventService eventService) throws DaoException {
		this.notificationService = notificationService;
		this.webPushManager = webPushManager;
		this.userService = userService;
		this.eventService = eventService;

		this.initializeNotificationsQueue();
		this.initWorkers();
	}

	/**
	 * Add the Notification from the DB to the queue
	 */
	private void initializeNotificationsQueue() throws DaoException {
		List<Notification> notifications = notificationService.getAllNotification();
		this.notificationsQueue.addAll(notifications);
		Application.logger.info("Init the queue with " + notifications.size() + " notifications");
	}

	private void initWorkers() {
		this.workersGroup = new ThreadGroup("Notification Workers");

		for (int i = 0; i < NUM_OF_THREAD; i++) {
			Thread worker = new Thread(this.workersGroup, new NotificationWorker(this.notificationsQueue,
					this.userService, this.webPushManager, this.notificationService), "Thread_" + i);
			worker.start();
		}
	}

	/**
	 * Add a new notification to the queue
	 *
	 * @param notification - and new notification
	 */
	public void addNotification(Notification notification) {
		this.notificationsQueue.add(notification);
		this.workersGroup.interrupt();
		Application.logger.info("Adding one notification to the queue");
	}

	/**
	 * Add multiple notifications to the queue
	 *
	 * @param notifications - the new notification
	 */
	public void addNotifications(List<Notification> notifications) {
		this.notificationsQueue.addAll(notifications);
		Application.logger.info("DEBUG: " + notifications.size());
		this.workersGroup.interrupt();
		Application.logger.info("Adding " + notifications.size() + " notifications to the queue");
	}

	/**
	 * Remove a notification from the queue
	 *
	 * @param notification - the notification to remove
	 */
	public void removeNotification(Notification notification) {
		Boolean changed = this.notificationsQueue.remove(notification);
		this.workersGroup.interrupt();
		Application.logger.info("Removing notification from the queue");
	}

	/**
	 * Remove multiple notifications
	 *
	 * @param notifications - the notifications to remove
	 */
	public void removeNotifications(List<Notification> notifications) {
		this.notificationsQueue.removeAll(notifications);
		this.workersGroup.interrupt();
		Application.logger.info("Removing " + notifications.size() + " notifications from the queue");
	}

	/**
	 * Replace the old notification with the new one
	 *
	 * @param notification - the modified notification
	 */
	public void updateNotification(Notification notification) {
		this.removeNotification(notification);
		this.addNotification(notification);
		this.workersGroup.interrupt();
		Application.logger.info("Updating notification in the queue");
	}

	/**
	 * Replace the old notifications with the new ones
	 *
	 * @param notifications - the modified notifications
	 */
	public void updateNotifications(List<Notification> notifications) {
		this.removeNotifications(notifications);
		this.addNotifications(notifications);
		this.workersGroup.interrupt();
		Application.logger.info("Updating " + notifications.size() + " notifications in the queue");
	}

	public void addNotificationsByEventId(Integer eventId) throws DaoException {
		Event event = this.eventService.getEventById(eventId);
		this.addNotifications(new ArrayList<Notification>(event.getNotifications()));
	}

}
