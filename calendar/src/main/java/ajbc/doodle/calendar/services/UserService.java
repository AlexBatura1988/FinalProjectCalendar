package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;

@Service
public class UserService {

	@Autowired
	@Qualifier("htUsDao")
	UserDao userDao;
	
	@Autowired
    @Qualifier("htEvDao")
    private EventDao eventDao;

    @Autowired
    private NotificationService notificationService;

	public void createUser(User user) throws DaoException {
		userDao.createUser(user);
	}
	
	public void createUserIfNotExists(User user) throws DaoException {
        if (!this.isUserExist(user.getEmail())) {
            userDao.createUser(user);
        }
    }

	public void createUsers(Set<User> users) throws DaoException {
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
	 * 
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
	
	
     // Return if the user exists in the DB
     
    public Boolean isUserExist(String email) {
        try {
            userDao.getUser(email);
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
		User user = getUser(userId);
		if (soft) {
			
			user.setDisable(1);
			updateUser(user);
		} else {
			for (Event event : user.getEvents()) {
				if (event.getOwnerId().equals(user.getUserId())) {
					event.removeGuest(user);
					event.setOwner(null);
                    eventDao.deleteEvent(event);
					
				}else {
                    event.removeGuest(user);
                    eventDao.updateEvent(event);
				}
				
			}
			userDao.deleteUser(user);
		}
	}

	public User updateUser(User user) throws DaoException {
		return userDao.updateUser(user);
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

	public List<User> getAllUsers() throws DaoException {
		return userDao.getAllUsers();
	}

	

	public List<User> getSubscribedUserByNotificationId(Integer notificationId) throws DaoException {
		return userDao.getSubscribedUserByNotificationId(notificationId);
	}

}
