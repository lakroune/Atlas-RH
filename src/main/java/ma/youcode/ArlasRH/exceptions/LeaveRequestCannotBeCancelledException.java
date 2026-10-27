package ma.youcode.ArlasRH.exceptions;

public class LeaveRequestCannotBeCancelledException extends RuntimeException {
    public LeaveRequestCannotBeCancelledException() {
        super();
    }

    public LeaveRequestCannotBeCancelledException(String message) {
        super(message);
    }

    public LeaveRequestCannotBeCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}
