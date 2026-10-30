
package ma.youcode.ArlasRH.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.EmployeeUpdateMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments/{idD}/employes")
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

        private final EmployeeService employeeService;
        private final EmployeeCreateMapper employeeCreateMapper;
        private final EmployeeUpdateMapper employeeUpdateMapper;

        public EmployeeController(
                        EmployeeService employeeService,
                        EmployeeCreateMapper employeeCreateMapper, EmployeeUpdateMapper employeeUpdateMapper) {
                this.employeeService = employeeService;
                this.employeeCreateMapper = employeeCreateMapper;
                this.employeeUpdateMapper = employeeUpdateMapper;
        }

        @Operation(summary = "Get all employees of a department", description = "Returns the paginated list of employees belonging to the selected department. You can filter by employee name and sort by any field.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))),
                        @ApiResponse(responseCode = "404", description = "Department not found")
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Page<EmployeeResponse> getAllEmployes(
                        @Parameter(description = "Department ID", required = true, example = "1") @PathVariable Long idD,
                        @Parameter(description = "Page number to retrieve", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Number of employees per page", example = "10") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Field used to sort the employees", example = "id") @RequestParam(defaultValue = "id") String sortBy,
                        @Parameter(description = "Sorting direction: asc or desc", example = "asc") @RequestParam(defaultValue = "asc") String direction,
                        @Parameter(description = "Optional employee name filter", example = "sarah") @RequestParam(required = false) String nom) {
                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                                direction.equalsIgnoreCase("desc")
                                                                ? Sort.Direction.DESC
                                                                : Sort.Direction.ASC,
                                                sortBy));
                return employeeService.getEmployeesByDepartmentId(
                                idD, nom, pageable);
        }

        @Operation(summary = "Create an employee", description = "Creates a new employee in the selected department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class)))
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public EmployeeResponse createEmployee(@PathVariable Long idD,
                        @Valid @RequestBody EmployeeCreateRequest request) {
                Employee employee = employeeCreateMapper.toEntity(request);
                return employeeService.createEmployee(idD, employee);
        }

        @Operation(summary = "Get an employee by ID", description = "Returns the employee matching the given ID within the department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employee found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class)))
        })
        @GetMapping("/{idE}")
        @ResponseStatus(HttpStatus.OK)
        public EmployeeResponse getEmployeeById(@PathVariable Long idD, @PathVariable Long idE) {
                return employeeService.getEmployeeById(idD, idE);
        }

        @Operation(summary = "Update an employee", description = "Updates the employee information for the given ID inside the department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class)))
        })
        @PatchMapping("/{idE}")
        @ResponseStatus(HttpStatus.OK)
        public EmployeeResponse updateEmployee(@PathVariable Long idD, @PathVariable Long idE,
                        @Valid @RequestBody EmployeeUpdateRequest request) {

                Employee employee = employeeUpdateMapper.toEntity(request);
                return employeeService.updateEmployee(idD, idE, employee);
        }

        @Operation(summary = "Delete an employee", description = "Deletes the employee matching the given ID in the department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Employee deleted successfully")
        })
        @DeleteMapping("/{idE}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ResponseEntity<Void> deleteEmployee(@PathVariable Long idD, @PathVariable Long idE) {

                employeeService.deleteEmployee(idD, idE);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Get an employee's leave balance", description = "Returns the leave balance of the employee within the department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employee leave balance retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                        @ApiResponse(responseCode = "404", description = "Employee or department not found")
        })
        @GetMapping("/{idE}/solde")
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<List<String>> getEmployeeLeaveBalance(@PathVariable Long idD, @PathVariable Long idE) {

                return ResponseEntity.ok(employeeService.getLeaveBalance());

        }

        @Operation(summary = "Get an employee's seniority leave balance", description = "Returns the seniority-based leave balance of the employee within the department.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employee seniority leave balance retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                        @ApiResponse(responseCode = "404", description = "Employee or department not found")
        })
        @GetMapping("/{idE}/ancien")
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<List<String>> getEmployeeLeaveBalanceAncien(@PathVariable Long idD,
                        @PathVariable Long idE) {
                return ResponseEntity.ok(employeeService.getLeaveBalanceAncien());

        }

}
