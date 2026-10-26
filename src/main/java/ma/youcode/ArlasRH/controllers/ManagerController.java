package ma.youcode.ArlasRH.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.services.EmployeeService;

@RestController
@RequestMapping("/api/v1/managers")
@Tag(name = "Managers", description = "Manager management APIs")
@RequiredArgsConstructor
public class ManagerController {

    private final EmployeeService employeeService;

    @Operation(summary = "Get employees by manager ID", description = "Returns the list of employees managed by the specified manager.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @GetMapping("/{managerId}/team")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByManagerId(
            @PathVariable Long managerId) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByManager_Id(managerId);
        return ResponseEntity.ok(employees);
    }
}
