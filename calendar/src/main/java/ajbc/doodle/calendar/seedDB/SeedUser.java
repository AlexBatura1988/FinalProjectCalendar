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
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.RepeatingOptions;
import ajbc.doodle.calendar.enums.Unit;
import ajbc.doodle.calendar.exceptions.NotAuthorizedException;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

@Component
public class SeedUser {

	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	

	@EventListener
	public void seedUserDB(ContextRefreshedEvent event) throws DaoException, NotAuthorizedException {
		seedUsers();
		seedEvents();

	}

	public void seedUsers() throws DaoException {

		userService.createUserIfNotExists(
				new User("Alex", "batura", "gmail.com", LocalDate.of(1988, 1, 4), LocalDate.of(2020, 2, 3), 0));
		userService.createUserIfNotExists(
				new User("Arina", "batura", "gmail1.com", LocalDate.of(1990, 1, 4), LocalDate.of(2021, 2, 3), 0));
		userService.createUserIfNotExists(
				new User("marina", "ttt", "gmail2.com", LocalDate.of(1990, 1, 5), LocalDate.of(2021, 6, 3), 0));

	}

	public void seedEvents() throws DaoException {
		List<User> users = userService.getAllUsers();

		for (User user : users) {
			if (user.getEvents().size() > 0) {
				return;
			}
		}

		eventService.addEvent(new Event(users.get(1), "test", true, LocalDateTime.of(2022, 7, 13, 12, 40),
				LocalDateTime.of(2022, 7, 13, 23, 0), "holon", "test description", RepeatingOptions.DAILY, users));

	}

}
