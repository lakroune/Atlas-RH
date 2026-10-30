package ma.youcode.ArlasRH.controllers;

import java.util.List;

import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.LeaveRequestAprouveRequest;
import ma.youcode.ArlasRH.services.EmployeeService;

@RestController
@RequestMapping("/api/v1/managers")
@Tag(name = "Managers", description = "Manager management APIs")
@RequiredArgsConstructor
public class ManagerController {

        private final EmployeeService employeeService;
        private final PathMatcher pathMatcher;

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

        @Operation(summary = "Assign employees to a manager", description = "Assigns a list of employee IDs to the selected manager.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Employees assigned successfully"),
                        @ApiResponse(responseCode = "404", description = "Manager or employee not found")
        })

        @PostMapping("/{managerId}/employees")
        public ResponseEntity<String> affecterEmployesAuManager(
                        @PathVariable Long managerId,
                        @RequestBody List<String> matricules) {

                employeeService.affecterEmployesAuManager(managerId, matricules);

                return ResponseEntity.ok("Employés affectés au manager avec succès");
        }

        @Operation(summary = "Get leaves en tatant requests by manager", description = "Returns all leave requests submitted by employees managed by the specified manager.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeaveRequestRespense.class)))),
                        @ApiResponse(responseCode = "404", description = "Manager not found")
        })

        @GetMapping("/{managerId}/demands")
        @ResponseStatus(HttpStatus.OK)
        public List<LeaveRequestRespense> getLeaveRequestsByManager(
                        @PathVariable Long managerId) {
                return employeeService.getLeaveRequestsByManager(managerId);
        }

        @Operation(summary = "Approve a leave request", description = "Approves a leave request for an employee managed by the specified manager.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Leave request approved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeaveRequestRespense.class)))),
                        @ApiResponse(responseCode = "404", description = "Manager or leave request not found")
        })

        @PatchMapping("/{managerId}/demands/{leaveRequestId}/approve")
        @ResponseStatus(HttpStatus.OK)
        public LeaveRequestRespense approveLeaveRequest(
                        @PathVariable Long managerId,
                        @PathVariable Long leaveRequestId,
                        @RequestBody LeaveRequestAprouveRequest leaveRequestAprouveRequest) {
                return employeeService.approveLeaveRequest(managerId, leaveRequestId, leaveRequestAprouveRequest);
        }

        @Operation(summary = "Reject a leave request", description = "Rejects a leave request for an employee managed by the specified manager.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Leave request rejected successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeaveRequestRespense.class)))),
                        @ApiResponse(responseCode = "404", description = "Manager or leave request not found")
        })

        @PatchMapping("/{managerId}/demands/{leaveRequestId}/reject")
        @ResponseStatus(HttpStatus.OK)
        public LeaveRequestRespense rejectLeaveRequest(
                        @PathVariable Long managerId,
                        @PathVariable Long leaveRequestId) {
                return employeeService.rejectLeaveRequest(managerId, leaveRequestId);
        }
}
