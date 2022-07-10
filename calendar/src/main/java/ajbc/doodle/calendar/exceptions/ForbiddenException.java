package ajbc.doodle.calendar.exceptions;

import org.springframework.stereotype.Service;

/**
 * An Exception for forbidden actions
 */
@Service
public class ForbiddenException extends Exception {
	public ForbiddenException() {
        super();
    }
    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException(Throwable cause) {
        super(cause);
    }
	

}
