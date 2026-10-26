package ma.youcode.ArlasRH.exceptions;

public class LeaveTypeNotFoundException extends RuntimeException {

    public LeaveTypeNotFoundException() {
        super();
    }

    public LeaveTypeNotFoundException(String message) {
        super(message);
    }

    public LeaveTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
