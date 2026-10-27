package ma.youcode.ArlasRH.exceptions;

public class LeaveRequestAlreadyCancelledException extends RuntimeException {
    public LeaveRequestAlreadyCancelledException() {
        super();
    }

    public LeaveRequestAlreadyCancelledException(String message) {
        super(message);
    }

    public LeaveRequestAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

}
