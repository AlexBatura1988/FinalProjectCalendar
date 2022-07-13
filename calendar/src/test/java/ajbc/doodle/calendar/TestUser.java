package ajbc.doodle.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

@TestInstance(Lifecycle.PER_CLASS)
class TestUser {
	
	private static  String firstName, lastName , email;
	private static LocalDate birthDate, joinDate;
	private static Integer disable;
	
	static {
		firstName = "Alex";
		lastName = "batura";
		email = "gmail.com";
		birthDate = LocalDate.of(1988, 1, 4);
		joinDate = LocalDate.of(2020, 2, 3);
		disable = 0;
	}
	
	User user;
	
	TestUser(){
		user = new User("Alex", "batura", "gmail.com", LocalDate.of(1988, 1, 4), LocalDate.of(2020, 2, 3), 0);
	}
	
	@Test
	void checkDefaultConstructor() {
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(email, user.getEmail());
		assertEquals(birthDate, user.getBirthDate());
		assertEquals(joinDate, user.getJoinDate());
		assertEquals(disable, user.getDisable());
		
	}
	
	@Test
	void checkFirstNameGetter() {
		assertEquals(firstName, user.getFirstName());
	}
	
	@Test
	void checkLastNameGetter() {
		assertEquals(lastName, user.getLastName());
	}
	
	@Test
	void checkEmailGetter() {
		assertEquals(email, user.getEmail());
	}
	
	@Test
	void checkBirthDateGetter() {
		assertEquals(LocalDate.of(1988, 1, 4), user.getBirthDate());
	}
	
	@Test
	void checkJoinDateGetter() {
		assertEquals(LocalDate.of(2020, 2, 3), user.getJoinDate());
	}
	
	
	@Test
	void checkEventsGetter() {
		assertTrue(user.getEvents().isEmpty());
	}
	
	
	
	
	

	

}
