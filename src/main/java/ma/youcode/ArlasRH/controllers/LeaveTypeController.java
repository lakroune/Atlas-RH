package ma.youcode.ArlasRH.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.services.LeaveTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "leave type", description = "Leave type management APIs")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    @Operation(summary = "Get all leave types", description = "Returns the list of available leave types.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave types retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeaveTypeResponse.class))))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LeaveTypeResponse> getAllLeaveType() {
        return leaveTypeService.getAllLeaveTypes();
    }






    

}
