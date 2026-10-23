package ma.youcode.ArlasRH.exceptions;

public class DepartmentAlreadyExistsException extends RuntimeException {

    public DepartmentAlreadyExistsException() {
        super();
    }

    public DepartmentAlreadyExistsException(String message) {
        super(message);
    }

    public DepartmentAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
