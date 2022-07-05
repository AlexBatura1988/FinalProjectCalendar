package ajbc.doodle.calendar.seedDB;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.UserService;


@Component
public class SeedUser {
	
	@Autowired
	private UserService userService;
	
	@EventListener
	public void seedUserDB(ContextRefreshedEvent event)  {
		try {
			seedUsers();
		} catch (DaoException e) {
			
			e.printStackTrace();
		}
	}

	public void seedUsers() throws DaoException {
		
		userService.hardDeleteAllUsers();
		userService.addUser(new User("Alex","batura","gmail.com",LocalDate.of(1988, 1, 4),LocalDate.of(2020, 2, 3),0));
		userService.addUser(new User("Arina","batura","gmail1.com",LocalDate.of(1990, 1, 4),LocalDate.of(2021, 2, 3),0));
		

		
		
	}
	
	
	
}
