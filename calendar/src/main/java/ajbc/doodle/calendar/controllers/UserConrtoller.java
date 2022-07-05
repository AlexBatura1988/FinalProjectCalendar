package ajbc.doodle.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import ajbc.doodle.calendar.services.UserService;

@RestController
@RequestMapping("/users")
public class UserConrtoller {

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody User user) throws DaoException {
		try {
			userService.addUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add user to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() throws DaoException {
		List<User> users = userService.getAllUsers();
		if (users == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.ok(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) throws DaoException {
		try {
			User user = userService.getUserById(id);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}

	}

	@RequestMapping(method = RequestMethod.PUT, path = "/id/{id}")
	public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Integer id) {
		try {
			user.setEmailId(id);
			userService.updateUser(user);
			user = userService.getUserById(id);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/id/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) throws DaoException {
		try {
			User user = userService.getUserById(id);
			userService.deleteUser(id);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
