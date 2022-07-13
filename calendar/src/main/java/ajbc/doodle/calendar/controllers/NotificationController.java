package ajbc.doodle.calendar.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.exceptions.ForbiddenException;
import ajbc.doodle.calendar.exceptions.NotAuthorizedException;
import ajbc.doodle.calendar.managers.NotificationManager;
import ajbc.doodle.calendar.managers.WebPushManager;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired
	NotificationService notificationService;
	@Autowired
	UserService userService;
	@Autowired
	EventService eventService;
	
	@Autowired
    WebPushManager webPushManager;
	
	@Autowired
    NotificationManager notificationManager;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllNotifications() throws DaoException {
		try {
			List<Notification> notifications = notificationService.getAllNotification();
			if (notifications == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

			return ResponseEntity.ok(notifications);
		} catch (DaoException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setData(e.getMessage());
			errMsg.setMessage("Failed to get all notifications from DB.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}

	}

	@RequestMapping(method = RequestMethod.GET, path = "/id/{notificationId}")
	public ResponseEntity<?> getAllNotifications(@PathVariable Integer notificationId) {
		try {
			Notification notification = notificationService.getNotificationById(notificationId);
			return ResponseEntity.ok(notification);
		} catch (DaoException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setData(e.getMessage());
			errMsg.setMessage("Failed to get notification from DB.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errMsg);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "event/{eventId}")
	public ResponseEntity<?> getNotificationsByEventId(@PathVariable Integer eventId) {
		try {
			Event event = eventService.getEventById(eventId);
			if (event == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(event.getActiveNotifications());
		} catch (DaoException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setData(e.getMessage());
			errMsg.setMessage("Failed to get all notifications from DB.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}

	}

	@RequestMapping(method = RequestMethod.POST, path = "/user/{userId}/event/{eventId}")
	public ResponseEntity<?> addNotification(@PathVariable Integer userId, @PathVariable Integer eventId,
			@RequestBody Notification notification) throws DaoException {
		try {
			User owner = userService.getUser(userId);
			Event event = eventService.getEventById(eventId);
			if (!owner.getUserId().equals(event.getOwnerId())) {
				throw new NotAuthorizedException("Only the owner of an event can add notifications");
			}

			notificationService.addNotification(owner, event, notification);
			notification = notificationService.getNotificationById(notification.getNotificationId());
			this.notificationManager.addNotification(notification);

			return ResponseEntity.status(HttpStatus.CREATED).body(notification);
		} catch (NotAuthorizedException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errMsg);
		} catch (ForbiddenException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMsg);

		} catch (DaoException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setData(e.getMessage());
			errMsg.setMessage("Failed to add notification to DB.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "multiple/user/{userId}/event/{eventId}")
	public ResponseEntity<?> addNotifications(@PathVariable Integer userId, @PathVariable Integer eventId,
			@RequestBody List<Notification> notifications) throws DaoException {
		try {
			User owner = userService.getUser(userId);
			Event event = eventService.getEventById(eventId);

			if (!owner.getUserId().equals(event.getOwnerId())) {
				throw new NotAuthorizedException("Only the owner of an event can add notifications");
			}

			for (Notification notification : notifications) {
				notificationService.addNotification(owner, event, notification);
			}
			List<Integer> notificationIds = notifications.stream().map(notification -> notification.getNotificationId()).toList();
			notifications = notificationService.getNotificationsByIds(notificationIds); 
			this.notificationManager.addNotifications(notifications);

			return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
		} catch (NotAuthorizedException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errMsg);

		} catch (ForbiddenException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMsg);
		} catch (DaoException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setData(e.getMessage());
			errMsg.setMessage("Failed to add notification to DB.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/user/{ownerId}")
	public ResponseEntity<?> updateNotification(@PathVariable Integer ownerId, @RequestBody Notification notification) {
		try {
			if (!notification.getOwnerId().equals(ownerId)) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setMessage("The user must be the owner of the notification");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			notificationService.updateNotification(notification);
			this.notificationManager.updateNotification(notification);
			return ResponseEntity.status(HttpStatus.OK).body(notification);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to update the event");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/multiple/user/{ownerId}")
	public ResponseEntity<?> updateNotifications(@PathVariable Integer ownerId,
			@RequestBody List<Notification> notifications) {
		try {
			for (Notification notification : notifications) {
				if (!notification.getOwnerId().equals(ownerId)) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setMessage("The user must be the owner of the notification");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
			}
			 List<Notification> newNotifications = new ArrayList<>();
			for (Notification notification : notifications) {
				newNotifications.add(notificationService.updateNotification(notification));
				
			}
			this.notificationManager.updateNotifications(newNotifications);
			return ResponseEntity.status(HttpStatus.OK).body(notifications);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to update the event");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/user/{ownerId}/id/{notificationId}")
	public ResponseEntity<?> deleteNotification(@PathVariable Integer ownerId, @PathVariable Integer notificationId,
			@RequestParam(defaultValue = "true") Boolean soft) {
		try {
			Notification notification = notificationService.getNotificationById(notificationId);

			if (!notification.getOwnerId().equals(ownerId)) {
				throw new NotAuthorizedException("Only the owner of an event can remove notifications");
			}

			 Notification removedNotification = notificationService.deleteNotification(notification, soft);
	            this.notificationManager.removeNotification(removedNotification);
			return ResponseEntity.ok("The Notification deleted");
		} catch (NotAuthorizedException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errMsg);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to delete the notification");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/user/{ownerId}")
	public ResponseEntity<?> deleteNotification(@PathVariable Integer ownerId,
			@RequestParam List<Integer> notificationIds, @RequestParam(defaultValue = "true") Boolean soft) {
		try {
			List<Notification> notifications = notificationService.getNotificationsByIds(notificationIds);

			for (Notification notification : notifications) {
				if (!notification.getOwnerId().equals(ownerId)) {
					throw new NotAuthorizedException("Only the owner of an event can remove notifications");
				}
			}

			for (Notification notification : notifications) {
				Notification removedNotification = notificationService.deleteNotification(notification, soft);
                this.notificationManager.removeNotification(removedNotification);
			}
			return ResponseEntity.ok("The Notifications deleted");
		} catch (NotAuthorizedException e) {
			ErrorMessage errMsg = new ErrorMessage();
			errMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errMsg);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to delete the notification");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}
	
	@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
    public byte[] publicSigningKey() {
        return webPushManager.getServerKeys().getPublicKeyUncompressed();
    }

    @GetMapping(path = "/publicSigningKeyBase64")
    public String publicSigningKeyBase64() {
        return webPushManager.getServerKeys().getPublicKeyBase64();
    }
	
	

}
