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
import ajbc.doodle.calendar.exceptions.ForbiddenException;
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
	            return ResponseEntity.ok(event.getNotifications());
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
    public ResponseEntity<?> addNotifications(@PathVariable Integer userId, @PathVariable Integer eventId, @RequestBody List<Notification> notifications) throws DaoException {
        try {
            User owner = userService.getUser(userId);
            Event event = eventService.getEventById(eventId);

            if (!owner.getUserId().equals(event.getOwnerId())) {
                throw new NotAuthorizedException("Only the owner of an event can add notifications");
            }

            for (Notification notification : notifications) {
                notificationService.addNotification(owner, event, notification);
            }

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
	
	

}
