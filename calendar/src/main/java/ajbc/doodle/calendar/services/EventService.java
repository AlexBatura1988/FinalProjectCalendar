package ajbc.doodle.calendar.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;


@Service
public class EventService {
	
	@Autowired
	@Qualifier("htEvDao")
	private EventDao eventDao;
	
	@Autowired
	@Qualifier("htUsDao")
	UserDao userDao;
	
	public void addEvent(Event event) throws DaoException {
		eventDao.addEvent(event);
	}
	
	public Event getEventbyId(Integer eventId) throws DaoException {
		return eventDao.getEventById(eventId);
	}
	
	public List<Event> getAllEvents() throws DaoException{
		return eventDao.getAllEvents();
	}
	
	
	
	

}
