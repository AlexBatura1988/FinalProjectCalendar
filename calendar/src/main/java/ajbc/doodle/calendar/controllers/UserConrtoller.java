package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import ajbc.doodle.calendar.entities.webpush.SubscriptionEndpoint;
import ajbc.doodle.calendar.services.UserService;

@RestController
@RequestMapping("/users")
public class UserConrtoller {

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user) throws DaoException {
		try {
			userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add user to db");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}

	}

	@RequestMapping(method = RequestMethod.POST, path = "/multiple")
	public ResponseEntity<?> createUsers(@RequestBody List<User> users) throws DaoException {
		try {
			userService.createUsers(users);
			return ResponseEntity.status(HttpStatus.CREATED).body(users);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add users to db");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() throws DaoException {
		List<User> users = userService.getAllUsers();
		if (users == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.ok(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/email/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws DaoException {
		try {
			User user = userService.getUser(email);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) throws DaoException {
		try {
			User user = userService.getUser(id);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}

	}

	@RequestMapping(method = RequestMethod.GET, path = "/event/{eventId}")
	public ResponseEntity<List<User>> getUsersByEventId(@PathVariable Integer eventId) throws DaoException {
		List<User> users = userService.getUsersByEventId(eventId);
		if (users == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/events")
	public ResponseEntity<?> getUsersByEventBetweenDates(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime)
			throws DaoException {
		if (startTime.compareTo(endTime) > 0) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage("startTime must be before endTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
		List<User> users = userService.getUsersByEventBetweenDates(startTime, endTime);
		if (users == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/exist/id/{userId}")
	public ResponseEntity<?> isUserExist(@PathVariable Integer userId) {
		return ResponseEntity.ok(userService.isUserExist(userId));
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/id/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody User user) {
		try {
			user.setUserId(userId);
			userService.updateUser(user);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateUsers(@RequestBody List<User> users) {
		try {
			// Validate that the users have the emailId field
			for (User user : users) {
				if (user.getUserId() == null) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setMessage("every user object must contain userId field");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
			}

			// Update the users
			for (User user : users) {
				userService.updateUser(user);
			}
			return ResponseEntity.status(HttpStatus.OK).body(users);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/id/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id, @RequestParam(defaultValue = "true") Boolean soft)
			throws DaoException {
		try {

			userService.deleteUser(id, soft);
			return ResponseEntity.ok("The User deleted");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/subscribe/{email}")
	public ResponseEntity<?> subscribe(@RequestBody Subscription subscription, @PathVariable String email) {
		try {
			User user = userService.getUser(email);
			userService.subscribeUser(subscription, user);
			System.out.println("user subscribed with email: "+ user.getEmail());
			return ResponseEntity.ok().body("User subscribed");
		} catch (DaoException e) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/unsubscribe")
	public ResponseEntity<?> unsubscribe(@RequestBody SubscriptionEndpoint subscription) {
		try {
			List<User> users = userService.getUsersByEndpoint(subscription.getEndpoint());
			for (User user : users) {
				userService.unsubscribeUser(user);
				
			}
			
			

			return ResponseEntity.ok().body("User unsubscribed");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/unsubscribe/{email}")
	public ResponseEntity<?> unsubscribeByEmail(@PathVariable String email) {
		try {
			User user = userService.getUser(email);
			userService.unsubscribeUser(user);
			System.out.println("user unsubscribed with email: " + user.getEmail());

			return ResponseEntity.ok().body("User unsubscribed");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, path = "/isSubscribed")
	public ResponseEntity<?> isSubscribed(@RequestBody SubscriptionEndpoint subscription) {
		try {
			return ResponseEntity.ok().body(userService.isSubscribed(subscription.getEndpoint()));
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
