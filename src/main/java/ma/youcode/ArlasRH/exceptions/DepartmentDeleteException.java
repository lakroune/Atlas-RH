package ma.youcode.ArlasRH.exceptions;

public class DepartmentDeleteException extends RuntimeException {

    public DepartmentDeleteException() {
        super();
    }

    public DepartmentDeleteException(String message) {
        super(message);
    }

    public DepartmentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
