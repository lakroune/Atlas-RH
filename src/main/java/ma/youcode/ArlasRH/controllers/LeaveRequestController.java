package ma.youcode.ArlasRH.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.services.LeaveRequestService;

@RestController
@RequestMapping("/Employees")
@Tag(name = "Leave Requests", description = "Leave request management APIs")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @Operation(summary = "Create a leave request", description = "Creates a leave request for a specific employee using the selected leave type and dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave request created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveRequestRespense.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PostMapping("/{idE}/demandes")
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequestRespense createLeaveRequest(
            @PathVariable Long idE,
            @RequestBody LeaveRequesCreatetRequest leaveRequest) {
        return leaveRequestService.createLeaveRequest(idE, leaveRequest);
    }
}
