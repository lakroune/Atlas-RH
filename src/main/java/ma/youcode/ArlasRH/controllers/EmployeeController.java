
package ma.youcode.ArlasRH.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ma.youcode.ArlasRH.dtos.EmployeeCreateRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.EmployeeUpdateRequest;
import ma.youcode.ArlasRH.mappers.EmployeeMapper;
import ma.youcode.ArlasRH.mappers.EmployeeUpdateMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;

@RestController
@RequestMapping("/api/v1/departments/{idD}/employes")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeUpdateMapper employeeUpdateMapper;

    public EmployeeController(
            EmployeeService employeeService,
            EmployeeMapper employeeMapper, EmployeeUpdateMapper employeeUpdateMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.employeeUpdateMapper = employeeUpdateMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployes(@PathVariable Long idD) {
        return employeeService.getEmployeesByDepartmentId(idD);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@PathVariable Long idD, @Valid @RequestBody EmployeeCreateRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        return employeeService.createEmployee(idD, employee);
    }

    @GetMapping("/{idE}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployeeById(@PathVariable Long idD, @PathVariable Long idE) {
        return employeeService.getEmployeeById(idD, idE);
    }

    @PatchMapping("/{idE}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable Long idD, @PathVariable Long idE, @Valid @RequestBody EmployeeUpdateRequest request) {

        Employee employee = employeeUpdateMapper.toEntity(request);
        return employeeService.updateEmployee(idD, idE, employee);
    }

    @DeleteMapping("/{idE}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long idD, @PathVariable Long idE) {

        employeeService.deleteEmployee(idD, idE);
        return ResponseEntity.noContent().build();
    }
}