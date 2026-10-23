package ma.youcode.ArlasRH.exceptions;

public class ContractNotFoundException extends RuntimeException {

    public ContractNotFoundException() {
        super();
    }

    public ContractNotFoundException(String message) {
        super(message);
    }

    public ContractNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
