package ajbc.doodle.calendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.entities.Event;

@Service
public class EventService {
	
	@Autowired
	@Qualifier("htEvDao")
	private EventDao eventDao;
	
	public void addEvent(Event event) throws DaoException {
		eventDao.addEvent(event);
	}
	
	public Event getEventbyId(Integer eventId) throws DaoException {
		return eventDao.getEventById(eventId);
	}
	
	

}
