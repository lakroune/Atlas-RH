package ma.youcode.ArlasRH.controllers;


import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.mappers.EmployeeMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/employes")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }


    @GetMapping
    public List<EmployeeResponse> getAllEmployes() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return employees;
    }


    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping("{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return new ResponseEntity<String>("Employees have been deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

        }
    }

}
