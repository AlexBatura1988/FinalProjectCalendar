package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;

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
	
	/**
     * Return if the user exists in the DB
     * @param userId - the user id
     * @return true if the user exists in the DB
     */
    public Boolean isUserExist(Integer userId) {
        try {
            userDao.getUser(userId);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }
	
	
	
	 public List<User> getUsersByEventId(Integer eventId) throws DaoException {
	        return userDao.getUsersByEventId(eventId);
	    }
	 public List<User> getUsersByIds(List<Integer> ids) throws DaoException {
	        return userDao.getUsersByIds(ids);
	    }
	 
	 public List<User> getUsersByEventBetweenDates(LocalDateTime stateTime, LocalDateTime endTime) throws DaoException {
	        return userDao.getUsersByEventBetweenDates(stateTime, endTime);
	    }
	 
	 public void deleteUser(Integer userId, Boolean soft) throws DaoException {
	        if (soft) {
	        	User user = getUser(userId);
	            user.setDisable(1);
	            updateUser(user);
	        } else {
	        	User user = getUser(userId);
//	            user.removeAllEvents();
//	            this.updateUser(user);
	            userDao.hardDeleteUser(userId);
	        }
	    }
	 
	 public void updateUser(User user) throws DaoException {
	        userDao.updateUser(user);
	    }
	 
	 public void subscribeUser(Subscription subscription, User user) throws DaoException {
	        user.setEndPoint(subscription.getEndpoint());
	        user.setP256dh(subscription.getKeys().getP256dh());
	        user.setAuth(subscription.getKeys().getAuth());
	        user.setIsSubscribed(true);
	        userDao.updateUser(user);
	    }

	    public void unsubscribeUser(User user) throws DaoException {
	        user.setEndPoint(null);
	        user.setP256dh(null);
	        user.setAuth(null);
	        user.setIsSubscribed(false);
	        userDao.updateUser(user);
	    }
	    public List<User> getUsersByEndpoint(String endpoint) throws DaoException {
	        return userDao.getUsersByEndpoint(endpoint);
	    }

	    public Boolean isSubscribed(String endpoint) throws DaoException {
	        return this.getUsersByEndpoint(endpoint).size() > 0;
	    }
	
	public List<User> getAllUsers() throws DaoException{
		return userDao.getAllUsers();
	}
	
	public void hardDeleteAllUsers() throws DaoException {
        userDao.hardDeleteAllUsers();
    }
	
	
	
	
	
	
	
	
	
	

}
