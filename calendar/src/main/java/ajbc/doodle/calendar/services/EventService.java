package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;


@Service
public class EventService {
	
	@Autowired
	@Qualifier("htEvDao")
	private EventDao eventDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("htUsDao")
	UserDao userDao;
	
	public void addEvent(Event event, Integer userId, List<Integer> guestsIds) throws DaoException {
		User owner = userService.getUser(userId);
		event.setOwner(owner);
		event.setGuests(List.of(owner));
		if (guestsIds != null) {
			List<User> guests = userService.getUsersByIds(guestsIds);
			//event.setGuests(guests);
			 event.addGuests(guests);
		}
		eventDao.addEvent(event);
	}
	
	public void addEvent(Event event) throws DaoException {
		eventDao.addEvent(event);
	}
	
	public void addEvents(List<Event> events, Integer userId, List<Integer> guestsIds) throws DaoException {
		for (Event event : events) {
			this.addEvent(event, userId, guestsIds);
		}
	}
	
	public Event getEventById(Integer eventId) throws DaoException {
		return eventDao.getEventById(eventId);
	}
	
	public List<Event> getAllEvents() throws DaoException{
		return eventDao.getAllEvents();
	}
	
	public List<Event> getAllEventsByUserId(Integer userId) throws DaoException {
        return eventDao.getAllEventsByUserId(userId);
    }
	public List<Event> getUpcomingEventsByUserId(Integer userId) throws DaoException {
        return eventDao.getUpcomingEventsByUserId(userId);
    }
	
	public List<Event> getUpcomingEventsByUserIdMinutesAndHours(Integer userId, Integer minutes, Integer hours) throws DaoException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endData = startDate.plusHours(hours).plusMinutes(minutes);
        return eventDao.getBetweenEventsByUserId(userId, startDate, endData);
    }
	
	public List<Event> getBetweenEventsByUserId(Integer userId, LocalDateTime startTime, LocalDateTime endTime) throws DaoException {
        return eventDao.getBetweenEventsByUserId(userId, startTime, endTime);
    }
	
	 public List<Event> getAllEventsEvents(LocalDateTime startTime, LocalDateTime endTime) throws DaoException {
	        return eventDao.getAllEventsEvents(startTime, endTime);
	    }
	 
	 public void updateEvent(Event event) throws DaoException {
	        Event oldEvent = this.getEventById(event.getEventId());
	        oldEvent.merge(event);
	        eventDao.updateEvent(oldEvent);
	    }
	
	
	
	

}
