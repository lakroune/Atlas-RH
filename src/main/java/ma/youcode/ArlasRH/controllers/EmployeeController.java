package ma.youcode.ArlasRH.controllers;


import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/employes")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployes() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }


    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        try {
            Employee employee1 = employeeService.createEmployee(employee);
            return new ResponseEntity<Employee>(employee1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEmploye(@PathVariable Long id) {

        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent())
            return new ResponseEntity<Employee>(employee.get(), HttpStatus.OK);
        else
            return new ResponseEntity<String>(" employe not exist", HttpStatus.NOT_FOUND);
    }


}
