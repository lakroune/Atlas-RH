package ma.youcode.ArlasRH.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapDepartmentExceptions() {
        assertResponse(handler.handleDepartmentExceptions(new DepartmentAlreadyExistsException("exists")), HttpStatus.CONFLICT, "exists");
        assertResponse(handler.handleDepartmentExceptions(new DepartmentNotFoundException("missing")), HttpStatus.NOT_FOUND, "missing");
        assertResponse(handler.handleDepartmentExceptions(new DepartmentDeleteException("used")), HttpStatus.BAD_REQUEST, "used");
    }

    @Test
    void shouldMapEmployeeExceptions() {
        assertResponse(handler.handleEmployeeExceptions(new EmployeeAlreadyExistsException("exists")), HttpStatus.CONFLICT, "exists");
        assertResponse(handler.handleEmployeeExceptions(new EmployeeNotFoundException("missing")), HttpStatus.NOT_FOUND, "missing");
        assertResponse(handler.handleEmployeeExceptions(new EmployeeAlreadyHasActiveContractException("active")), HttpStatus.BAD_REQUEST, "active");
    }

    @Test
    void shouldMapContractExceptions() {
        assertResponse(handler.handleContractExceptions(new ContractNotFoundException("missing")), HttpStatus.NOT_FOUND, "missing");
        assertResponse(handler.handleContractExceptions(new ContractAlreadyTerminatedException("ended")), HttpStatus.CONFLICT, "ended");
    }

    @Test
    void shouldMapManagerAndLeaveTypeExceptions() {
        assertResponse(handler.handleManagerExceptions(new ManagerNotFoundException("manager")), HttpStatus.NOT_FOUND, "manager");
        assertResponse(handler.handleLeaveTypeExceptions(new LeaveTypeNotFoundException("type")), HttpStatus.NOT_FOUND, "type");
    }

    @Test
    void shouldMapLeaveRequestExceptions() {
        assertResponse(handler.handleLeaveRequestExceptions(new LeaveRequestNotFoundException("missing")), HttpStatus.NOT_FOUND, "missing");
        assertResponse(handler.handleLeaveRequestExceptions(new LeaveRequestCannotBeCancelledException("cannot")), HttpStatus.CONFLICT, "cannot");
        assertResponse(handler.handleLeaveRequestExceptions(new LeaveRequestAlreadyCancelledException("cancelled")), HttpStatus.CONFLICT, "cancelled");
        assertResponse(handler.handleLeaveRequestExceptions(new LeaveRequestAlreadyExistException("exists")), HttpStatus.CONFLICT, "exists");
    }

    private void assertResponse(ResponseEntity<Map<String, String>> response, HttpStatus status, String message) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).containsEntry("message", message);
    }
}
