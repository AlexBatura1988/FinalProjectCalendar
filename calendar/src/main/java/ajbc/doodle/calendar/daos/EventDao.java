package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface EventDao {

	// CRUD operations
	@Transactional(readOnly = false)
	public default void addEvent(Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateEvent(Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Event getEventById(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void deleteEventById(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void hardDeleteAllEvents() throws  DaoException {
		throw new DaoException("Method not implemented");
	}
	
	

	// Queries
	
	public default List<Event> getAllEvents() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getUpcomingEventsByUserId(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getBetweenEventsByUserId(Integer userId, LocalDateTime startTime, LocalDateTime endTime) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getAllEventsEvents(LocalDateTime startTime, LocalDateTime endTime) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	public default long count() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getDiscontinuedEvents() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default List<Event> getAllEventsByUserId(Integer userId) throws DaoException{
		throw new DaoException("Method not implemented");
	}

}
