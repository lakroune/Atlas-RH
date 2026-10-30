package ma.youcode.ArlasRH.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.services.LeaveRequestService;

import java.util.List;

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

    @Operation(summary = "Afficher les demandes", description = "Afficher les demandes de congé pour un employé.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des demandes récupérée avec succès", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeaveRequestRespense.class)))),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @GetMapping("/{idE}/demandes")
    @ResponseStatus(HttpStatus.OK)
    public List<LeaveRequestRespense> getAllLeaveRequest(@PathVariable Long idE) {
        return leaveRequestService.getAllLeaveRequest(idE);
    }

    @PatchMapping("/{idE}/demandes/{idLeave}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public LeaveRequestRespense cancelLeaveRequest(@PathVariable Long idE, @PathVariable Long idLeave) {
        return leaveRequestService.cancelLeaveRequest(idLeave,idE);
    }
}
