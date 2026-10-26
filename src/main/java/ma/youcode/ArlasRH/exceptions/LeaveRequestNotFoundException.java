package ma.youcode.ArlasRH.exceptions;

public class LeaveRequestNotFoundException extends RuntimeException {

    public LeaveRequestNotFoundException() {
        super();
    }

    public LeaveRequestNotFoundException(String message) {
        super(message);
    }

    public LeaveRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
