package ajbc.doodle.calendar.seedDB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.RepeatingOptions;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;



@Component
public class SeedUser {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	
	@EventListener
	public void seedUserDB(ContextRefreshedEvent event)  {
		try {
			seedUsers();
			seedEvents();
		} catch (DaoException e) {
			
			e.printStackTrace();
		}
	}

	

	public void seedUsers() throws DaoException {
		
		//userService.hardDeleteAllUsers();
		userService.addUser(new User("Alex","batura","gmail.com",LocalDate.of(1988, 1, 4),LocalDate.of(2020, 2, 3),0));
		userService.addUser(new User("Arina","batura","gmail1.com",LocalDate.of(1990, 1, 4),LocalDate.of(2021, 2, 3),0));
			
	}
	
	public void seedEvents() throws DaoException {
		List<User> users = userService.getAllUsers();
		
		eventService.addEvent(new Event(users.get(2).getEmailId(), "test", true, LocalDateTime.of(2022, 8, 7,6,0),
				LocalDateTime.of(2022, 9, 7,5,0),  "addres", "test description",
				RepeatingOptions.DAILY, 0));
		
	}
	
	
	
}
