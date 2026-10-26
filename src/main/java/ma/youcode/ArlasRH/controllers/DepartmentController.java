package ma.youcode.ArlasRH.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Departments", description = "Department management APIs")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @Operation(
        summary = "Create a department",
        description = "Creates a new department with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Department created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartmentResponse.class)
            )
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@RequestBody Department department) {
        return departmentService.createDepartement(department);
    }

   @Operation(
    summary = "Get all departments",
    description = "Retrieve the list of all departments."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Departments retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = DepartmentResponse.class)
                )
            )
        )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentService.getAllDepartements();
    }

    @Operation(
        summary = "Delete a department",
        description = "Deletes a department by its identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Department deleted successfully"
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteDepartments(@PathVariable Long id) {
        try {
            departmentService.deleteDepartement(id);
            return new ResponseEntity<String>("The department has been deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @Operation(
        summary = "Update a department",
        description = "Updates an existing department by its identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Department updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartmentResponse.class)
            )
        )
    })
    @PatchMapping
    @RequestMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentResponse updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartement(id, department);
    }

    @Operation(
        summary = "Get a department by ID",
        description = "Returns the department matching the given identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Department found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartmentResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentResponse getDepartmentByID(@PathVariable Long id) {
        return departmentService.getDepartementById(id);

    }
}
