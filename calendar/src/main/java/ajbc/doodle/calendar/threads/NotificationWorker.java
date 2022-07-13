package ajbc.doodle.calendar.threads;

import ajbc.doodle.calendar.Application;
import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.managers.WebPushManager;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * A class for a worker that will send push notification about event
 * notification
 */
public class NotificationWorker implements Runnable {

	static private final int DEFAULT_SLEEP_TIME = 1000 * 60; // 1 min

	private final UserService userService;

	private final WebPushManager webPushManager;

	private final NotificationService notificationService;

	/**
	 * The blocking queue with the notifications
	 */
	private final BlockingQueue<Notification> queue;

	public NotificationWorker(BlockingQueue<Notification> queue, UserService userService, WebPushManager webPushManager,
			NotificationService notificationService) {
		this.queue = queue;
		this.userService = userService;
		this.webPushManager = webPushManager;
		this.notificationService = notificationService;
	}

	@Override
	public void run() {
		Application.logger.info(Thread.currentThread().getName() + ": started");
		while (true) {
			Notification notification = this.queue.poll();
			
			try {
				if (notification == null) {
					Application.logger.info(Thread.currentThread().getName()
							+ ": The queue is empty. going to sleep for " + DEFAULT_SLEEP_TIME + " milliseconds");
					Thread.sleep(DEFAULT_SLEEP_TIME);
				} else if(notification.getDisable() == 1) {
					// ignore non-active notifications
					Application.logger.warn(Thread.currentThread().getName() + ": got a non-active notification: " + notification.getNotificationId());
				} else {
					Application.logger.info("DEBUG: polling notification id: " + notification.getNotificationId() + " notification time: " + notification.getNotificationDateTime());
					LocalDateTime now = LocalDateTime.now();
					if (now.compareTo(notification.getNotificationDateTime()) >= 0) {
						// Send push message
						List<User> users = this.userService
								.getSubscribedUserByNotificationId(notification.getNotificationId());

						if (users.size() > 0) {
							List<User> failedUsers = this.webPushManager.sendPushMessageToAllSubscribers(users,
									notification.getEvent());
							// users with invalid endpoint
							for (User user : failedUsers) {
								this.userService.unsubscribeUser(user);
							}
						} else {
							Application.logger.info(Thread.currentThread().getName()
									+ ": no user is subscribe to eventId: " + notification.getEventId());
						}
						Application.logger.info("DEBUG ----------");
						this.notificationService.deleteNotification(notification, true);

					} else {
						this.queue.add(notification);
						Long remainingSeconds = notification.getRemainingSecondsToNotification() * 1000 + 1000; // add 1 sec to different from lower quantity
						Application.logger.info(Thread.currentThread().getName()
								+ ": the notification time is not now. going to sleep for " + remainingSeconds
								+ " milliseconds");
						Thread.sleep(remainingSeconds);
					}
				}
			} catch (InterruptedException e) {
				Application.logger.info(Thread.currentThread().getName()
						+ ": an InterruptedException occurred. going to check the queue again");
			} catch (DaoException e) {
				Application.logger.error(e.getMessage());
				Application.logger.error(Thread.currentThread().getName()
						+ ": can't find users to notify. removing the notification from the queue.");
				this.queue.remove(notification);
			} catch (JsonProcessingException e) {
				Application.logger.error(Thread.currentThread().getName() + ": can't parse the message");
				throw new RuntimeException(e);
			}
		}
	}

}
