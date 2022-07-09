package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.User;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface UserDao {

	// CRUD operations

	@Transactional(readOnly = false)
	public default void createUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default User getUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default User getUser(String email) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default List<User> getUsersByEventId(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	 public default List<User> getUsersByIds(List<Integer> ids) throws DaoException {
	        throw new DaoException("Method not implemented");
	    }

	public default List<User> getUsersByEventBetweenDates(LocalDateTime stateDate, LocalDateTime endDate)
			throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

//	@Transactional(readOnly = false)
//	public default void softDeleteUser(Integer userId) throws DaoException {
//		throw new DaoException("Method not implemented");
//	}

	@Transactional(readOnly = false)
	public default void hardDeleteUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	// QUERIES

	public default List<User> getAllUsers() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default List<User> getDiscontinuedUsers() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void hardDeleteAllUsers() throws DaoException {
		throw new DaoException("Method not implemented");
	}

}
