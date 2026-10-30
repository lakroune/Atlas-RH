package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

@Component
public class LeaveRequestCreateMapper {

    public LeaveRequest toEntity(LeaveRequesCreatetRequest request) {
        if (request == null) {
            return null;
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        // leaveRequest.setWorkingDay(request.getWorkingDay());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus(LeaveStatus.EN_ATTENTE);

        LeaveType leaveType = new LeaveType();
        leaveType.setId(request.getLeaveTypeId());
        leaveRequest.setLeaveType(leaveType);

        return leaveRequest;
    }

    public LeaveRequestRespense toResponse(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }

        LeaveRequestRespense response = new LeaveRequestRespense();
        response.setId(leaveRequest.getId());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setWorkingDay(leaveRequest.getWorkingDay());
        response.setReason(leaveRequest.getReason());
        response.setStatus(leaveRequest.getStatus());
        response.setManagerComment(leaveRequest.getManagerComment());
        response.setDecisionDate(leaveRequest.getDecisionDate());
        response.setEmployeeId(leaveRequest.getEmployee().getId());
        response.setLeaveTypeId(leaveRequest.getLeaveType().getId());
        return response;
    }
}
