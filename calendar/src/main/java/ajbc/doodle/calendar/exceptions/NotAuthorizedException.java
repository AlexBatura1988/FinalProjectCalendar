package ajbc.doodle.calendar.exceptions;

import org.springframework.stereotype.Service;

/**
 * 
 * An Exception for unauthorized actions
 *
 */
@Service
public class NotAuthorizedException extends Exception {
	
	public NotAuthorizedException() {
        super();
    }
    public NotAuthorizedException(String message) {
        super(message);
    }
    public NotAuthorizedException(Throwable cause) {
        super(cause);
    }

}
