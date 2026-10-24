
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Employees", description = "Employee management APIs")
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

    @Operation(
            summary = "Get all employees of a department",
            description = "Returns the list of employees belonging to the given department."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class))
                    )
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployes(@PathVariable Long idD) {
        return employeeService.getEmployeesByDepartmentId(idD);
    }

    @Operation(
            summary = "Create an employee",
            description = "Creates a new employee in the selected department."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class)
                    )
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@PathVariable Long idD, @Valid @RequestBody EmployeeCreateRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        return employeeService.createEmployee(idD, employee);
    }

    @Operation(
            summary = "Get an employee by ID",
            description = "Returns the employee matching the given ID within the department."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class)
                    )
            )
    })
    @GetMapping("/{idE}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployeeById(@PathVariable Long idD, @PathVariable Long idE) {
        return employeeService.getEmployeeById(idD, idE);
    }

    @Operation(
            summary = "Update an employee",
            description = "Updates the employee information for the given ID inside the department."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class)
                    )
            )
    })
    @PatchMapping("/{idE}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable Long idD, @PathVariable Long idE, @Valid @RequestBody EmployeeUpdateRequest request) {

        Employee employee = employeeUpdateMapper.toEntity(request);
        return employeeService.updateEmployee(idD, idE, employee);
    }

    @Operation(
            summary = "Delete an employee",
            description = "Deletes the employee matching the given ID in the department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully")
    })
    @DeleteMapping("/{idE}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long idD, @PathVariable Long idE) {

        employeeService.deleteEmployee(idD, idE);
        return ResponseEntity.noContent().build();
    }
}