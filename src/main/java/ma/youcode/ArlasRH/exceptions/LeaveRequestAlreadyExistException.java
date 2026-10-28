package ma.youcode.ArlasRH.exceptions;

public class LeaveRequestAlreadyExistException extends RuntimeException {

    public  LeaveRequestAlreadyExistException(){
        super();
    }
    public  LeaveRequestAlreadyExistException(String message){
        super(message);

    }
}
