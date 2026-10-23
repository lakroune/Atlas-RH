package ma.youcode.ArlasRH.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<Map<String, String>> handleDepartmentAlreadyExists(
            DepartmentAlreadyExistsException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
    }
}
