package ma.youcode.ArlasRH.controllers;

import jakarta.websocket.server.PathParam;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {

        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {

        try {
            Department department1 = departmentService.createDepartement(department);
            return new ResponseEntity<>(department1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartements();
        return ResponseEntity.ok(departments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartments(@PathVariable Long id) {
        try {
            departmentService.deleteDepartement(id);
            return new ResponseEntity<String>("The department has been deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {

        Optional<Department> department = departmentService.getDepartementById(id);
        if (department.isPresent())
            return new ResponseEntity<Department>(department.get(), HttpStatus.OK);
        else
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
    }
}
