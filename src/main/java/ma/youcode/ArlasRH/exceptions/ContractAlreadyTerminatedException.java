package ma.youcode.ArlasRH.exceptions;

public class ContractAlreadyTerminatedException extends RuntimeException {

    public ContractAlreadyTerminatedException() {
        super();
    }

    public ContractAlreadyTerminatedException(String message) {
        super(message);
    }

    public ContractAlreadyTerminatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
