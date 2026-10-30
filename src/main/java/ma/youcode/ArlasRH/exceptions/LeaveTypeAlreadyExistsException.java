package ma.youcode.ArlasRH.exceptions;

public class LeaveTypeAlreadyExistsException  extends RuntimeException {
    public LeaveTypeAlreadyExistsException() {
        super();
    }

    public LeaveTypeAlreadyExistsException(String message) {
        super(message);
    }

    public LeaveTypeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
