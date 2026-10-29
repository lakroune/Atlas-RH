package ma.youcode.ArlasRH.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.dtos.LeaveTypeRequest;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.mappers.LeaveTypeMapper;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.services.LeaveTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Leave type", description = "Leave type management APIs")
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


    @Operation(
            summary = "Create a leave type",
            description = "Creates a leave type with its name and annual leave allowance."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave type created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "The request body is invalid")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveTypeResponse createLeaveType(@Valid  @RequestBody LeaveTypeRequest leaveTypeRequest){
        LeaveType leaveType = LeaveTypeMapper.toEntity( leaveTypeRequest);
        return  leaveTypeService.createLeaveType(leaveType);
    }




    

}
