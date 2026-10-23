package ma.youcode.ArlasRH.exceptions;

public class EmployeeAlreadyHasActiveContractException extends RuntimeException {

    public EmployeeAlreadyHasActiveContractException() {
        super();
    }

    public EmployeeAlreadyHasActiveContractException(String message) {
        super(message);
    }

    public EmployeeAlreadyHasActiveContractException(String message, Throwable cause) {
        super(message, cause);
    }
}
