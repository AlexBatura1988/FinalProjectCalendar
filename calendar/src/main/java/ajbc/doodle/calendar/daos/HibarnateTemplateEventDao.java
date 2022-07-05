package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;

@SuppressWarnings("unchecked")
@Repository("htEvDao")
public class HibarnateTemplateEventDao implements EventDao {
	
	@Autowired
	private HibernateTemplate template;

	@Override
	public void addEvent(Event event) throws DaoException {
		template.persist(event);
	}

	@Override
	public void updateEvent(Event event) throws DaoException {
		// TODO Auto-generated method stub
		EventDao.super.updateEvent(event);
	}

	@Override
	public Event getEventById(Integer eventId) throws DaoException {
		Event event = template.get(Event.class, eventId);
		if (event == null)
			throw new DaoException("No  event in the DB");
		return event;
	}

	@Override
	public void deleteEventById(Integer eventId) throws DaoException {
		// TODO Auto-generated method stub
		EventDao.super.deleteEventById(eventId);
	}

	@Override
	public void hardDeleteAllEvents() throws DaoException {
		// TODO Auto-generated method stub
		EventDao.super.hardDeleteAllEvents();
	}

	@Override
	public List<Event> getAllEvents() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		return (List<Event>) template.findByCriteria(criteria);
	}

	@Override
	public long count() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		criteria.setProjection(Projections.rowCount());
		return (long)template.findByCriteria(criteria).get(0);
	}

	@Override
	public List<Event> getDiscontinuedEvents() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		criteria.add(Restrictions.eq("discontinued", 1));
		return (List<Event>)template.findByCriteria(criteria);
	}
	

}
