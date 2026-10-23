package ma.youcode.ArlasRH.exceptions;

public class EmployeeAlreadyExistsException extends RuntimeException {

    public EmployeeAlreadyExistsException() {
        super();
    }

    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }

    public EmployeeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
