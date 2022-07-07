package ajbc.doodle.calendar.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	@RequestMapping(method = RequestMethod.POST, path = "/{id}")
	public ResponseEntity<?> addEvent(@RequestBody Event event, @PathVariable Integer userId) {
		try {
			eventService.addEvent( event);
			event = eventService.getEventbyId(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
//	@RequestMapping(method = RequestMethod.GET)
//	public ResponseEntity<List<Event>> getAllEventss()() throws DaoException {
//		List<Event> events = eventService.getAllEvents();
//		if (events == null)
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		return ResponseEntity.ok(events);
//	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Event>> getAllEventss(@RequestParam Map<String, String> map) throws DaoException {
		List<Event> events;
			events = eventService.getAllEvents();
		if (events == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(events);
	}

	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) throws DaoException {
		try {
			Event event = eventService.getEventbyId(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}

	}
	

}
