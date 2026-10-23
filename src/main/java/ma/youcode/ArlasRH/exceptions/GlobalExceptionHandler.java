package ma.youcode.ArlasRH.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DepartmentAlreadyExistsException.class,
            DepartmentNotFoundException.class,
            DepartmentDeleteException.class
    })
    public ResponseEntity<Map<String, String>> handleDepartmentExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof DepartmentAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if (e instanceof DepartmentNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof DepartmentDeleteException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(status)
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler({
            EmployeeAlreadyExistsException.class,
            EmployeeNotFoundException.class,
            EmployeeAlreadyHasActiveContractException.class
    })
    public ResponseEntity<Map<String, String>> handleEmployeeExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof EmployeeAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if (e instanceof EmployeeNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof EmployeeAlreadyHasActiveContractException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(status)
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler({
            ContractNotFoundException.class,
            ContractAlreadyTerminatedException.class
    })
    public ResponseEntity<Map<String, String>> handleContractExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof ContractNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof ContractAlreadyTerminatedException) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity
                .status(status)
                .body(Map.of("message", e.getMessage()));
    }
}
