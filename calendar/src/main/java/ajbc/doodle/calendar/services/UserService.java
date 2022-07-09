package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.User;

@Service
public class UserService {
	
	@Autowired
	@Qualifier("htUsDao")
	UserDao userDao;
	
	
	
	public void createUser(User user) throws DaoException {
		userDao.createUser(user);
	}
	
	public void createUsers(List<User> users) throws DaoException {
        for (User user : users) {
            this.createUser(user);
        }
    }
	
	public User getUser(Integer userId) throws DaoException {
		return userDao.getUser(userId);
	}
	
	public User getUser(String email) throws DaoException {
        return userDao.getUser(email);
    }
	
	 public List<User> getUsersByEventId(Integer eventId) throws DaoException {
	        return userDao.getUsersByEventId(eventId);
	    }
	 
	 public List<User> getUsersByEventBetweenDates(LocalDateTime stateTime, LocalDateTime endTime) throws DaoException {
	        return userDao.getUsersByEventBetweenDates(stateTime, endTime);
	    }
	 
	 public void deleteUser(Integer userId, Boolean soft) throws DaoException {
	        if (soft) {
	            userDao.softDeleteUser(userId);
	        } else {
	            userDao.hardDeleteUser(userId);
	        }
	    }
	 
	 public void updateUser(User user) throws DaoException {
	        userDao.updateUser(user);
	    }
	
	public List<User> getAllUsers() throws DaoException{
		return userDao.getAllUsers();
	}
	
	public void hardDeleteAllUsers() throws DaoException {
        userDao.hardDeleteAllUsers();
    }
	
	
	
	
	
	
	
	
	
	

}
