package ajbc.doodle.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.exceptions.NotAuthorizedException;
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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Notification>> getAllNotifications() throws DaoException {
		List<Notification> notifications = notificationService.getAllNotification();
		if (notifications == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.ok(notifications);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/user/{userId}/event/{eventId}")
	public ResponseEntity<?> addNotification(@PathVariable Integer userId, @PathVariable Integer eventId,
			@RequestBody Notification notification) throws DaoException {
		try {
			 User owner = userService.getUser(userId);
			 Event event = eventService.getEventById(eventId);
			 notification.setOwner(owner);
	            notification.setEvent(event);
			notificationService.addNotification(notification);
			return ResponseEntity.status(HttpStatus.CREATED).body(notification);
		} catch (NotAuthorizedException e) {
			ErrorMessage errMsg = new ErrorMessage();
            errMsg.setData(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errMsg);
		} catch (DaoException e) {
            ErrorMessage errMsg = new ErrorMessage();
            errMsg.setData(e.getMessage());
            errMsg.setMessage("Failed to add notification to DB.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
        }
	}

}
