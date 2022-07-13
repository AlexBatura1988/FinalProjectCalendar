package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.managers.NotificationManager;
import ajbc.doodle.calendar.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private NotificationManager notificationManager;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllEvents() throws DaoException {
		List<Event> events = eventService.getAllEvents();
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/user/{userId}")
	public ResponseEntity<?> getAllEventsByUserId(@PathVariable Integer userId) throws DaoException {
		List<Event> events = eventService.getAllEventsByUserId(userId);
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/upcoming/user/{userId}")
	public ResponseEntity<?> getUpcomingEventsByUserId(@PathVariable Integer userId) throws DaoException {
		List<Event> events = eventService.getUpcomingEventsByUserId(userId);
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/upcoming_from/user/{userId}")
	public ResponseEntity<?> getUpcomingEventsByUserIdMinutesAndHours(@PathVariable Integer userId,
			@RequestParam Integer minutes, @RequestParam Integer hours) throws DaoException {
		List<Event> events = eventService.getUpcomingEventsByUserIdMinutesAndHours(userId, minutes, hours);
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/between/user/{userId}")
	public ResponseEntity<?> getBetweenEventsByUserId(@PathVariable Integer userId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime)
			throws DaoException {

		if (startTime.compareTo(endTime) > 0) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage("startTime must be before endTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		List<Event> events = eventService.getBetweenEventsByUserId(userId, startTime, endTime);
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/between")
	public ResponseEntity<?> getAllEventsEvents(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime)
			throws DaoException {
		if (startTime.compareTo(endTime) > 0) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage("startTime must be before endTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		List<Event> events = eventService.getAllEventsEvents(startTime, endTime);
		if (events == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/user/{userId}")
	public ResponseEntity<?> addEventByUser(@RequestBody Event event, @PathVariable Integer userId,
			@RequestParam(required = false) List<Integer> guestsIds) {
		try {
			eventService.addEvent(event, userId, guestsIds);
			this.notificationManager.addNotificationsByEventId(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/multiple/user/{userId}")
	public ResponseEntity<?> addEventsByUser(@RequestBody List<Event> events, @PathVariable Integer userId,
			@RequestParam(required = false) List<Integer> guestsIds) {
		try {
			eventService.addEvents(events, userId, guestsIds);
			
			for(Event event : events) {
				this.notificationManager.addNotifications(new ArrayList<Notification>(event.getNotifications()));
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).body(events);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) throws DaoException {
		try {
			Event event = eventService.getEventById(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}

	}

	@RequestMapping(method = RequestMethod.PUT, path = "/user/{ownerId}/eventId/{eventId}")
	public ResponseEntity<?> updateEvent(@PathVariable Integer ownerId, @PathVariable Integer eventId,
			@RequestBody Event event) {
		try {

			if (!event.getOwnerId().equals(ownerId)) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setMessage("The user must be the owner of the event");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			event.setEventId(eventId);
			event = eventService.updateEvent(event);
			return ResponseEntity.status(HttpStatus.OK).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to update the event");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "multiple/user/{ownerId}")
	public ResponseEntity<?> updateEvents(@PathVariable Integer ownerId, @RequestBody List<Event> events) {
		try {
			// Validate that the users have the emailId field
			for (Event event : events) {
				if (event.getEventId() == null) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setMessage("every event object must contain an eventId field");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

				if (!event.getOwnerId().equals(ownerId)) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setMessage("The user must be the owner of every event");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
			}
			List<Event> newEvents = new ArrayList<>();

			// Update the events
			for (Event event : events) {
				newEvents.add(eventService.updateEvent(event));
				 
			}
			return ResponseEntity.status(HttpStatus.OK).body(newEvents);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("failed to update the events");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/user/{ownerId}/id/{eventId}")
	public ResponseEntity<?> deleteEvent(@PathVariable Integer ownerId, @PathVariable Integer eventId,
			@RequestParam(defaultValue = "true") Boolean soft) throws DaoException {
		try {
			Event event = eventService.getEventById(eventId);

			if (!event.getOwnerId().equals(ownerId)) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setMessage("The user must be the owner of the event");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			eventService.deleteEvent(event, soft);
			return ResponseEntity.ok("The Event deleted");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/multiple/user/{ownerId}")
	public ResponseEntity<?> deleteEvents(@PathVariable Integer ownerId, @RequestParam List<Integer> eventIds,
			@RequestParam(defaultValue = "true") Boolean soft) throws DaoException {
		try {
			List<Event> events = eventService.getEventsByIds(eventIds);
			for (Event event : events) {
				if (!event.getOwnerId().equals(ownerId)) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setMessage("The user must be the owner of every event");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
			}
			for (Event event : events) {
				eventService.deleteEvent(event, soft);

			}
			return ResponseEntity.ok("The Events deleted");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
