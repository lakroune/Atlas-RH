
package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.EmployeeRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.mappers.EmployeeMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments/{idD}/employes")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(
            EmployeeService employeeService,
            EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployes(@PathVariable Long idD) {
        return employeeService.getEmployeesByDepartmentId(idD);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@PathVariable Long idD, @Valid @RequestBody EmployeeRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        return employeeService.createEmployee(idD, employee);
    }

    @GetMapping("/{idE}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployeeById(@PathVariable Long idD, @PathVariable Long idE) {
        return employeeService.getEmployeeById(idD, idE);
    }

    @DeleteMapping("/{idE}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long idD, @PathVariable Long idE) {

        employeeService.deleteEmployee(idD, idE);
        return ResponseEntity.noContent().build();
    }
}