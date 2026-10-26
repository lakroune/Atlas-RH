package ma.youcode.ArlasRH.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.services.LeaveRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Employees")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping("/{idE}/demandes")
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequest createLeaveRequest(
            @PathVariable Long idE,
            @RequestBody LeaveRequest leaveRequest) {
        return leaveRequestService.createLeaveRequest(idE, leaveRequest);
    }
}
