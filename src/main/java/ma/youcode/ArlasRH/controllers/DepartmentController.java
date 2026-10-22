package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
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
    private final DepartmentMapper departmentMapper;


    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {

        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping
    public DepartmentResponse createDepartment(@RequestBody Department department) {
        return departmentService.createDepartement(department);
    }


    @GetMapping
    public List<DepartmentResponse> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartements();
        return departments;
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
    public DepartmentResponse getDepartmentByID(@PathVariable Long id) {
        return departmentService.getDepartementById(id);

    }
}
