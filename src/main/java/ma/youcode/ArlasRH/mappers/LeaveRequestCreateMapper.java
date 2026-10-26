package ma.youcode.ArlasRH.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.models.LeaveRequest;

@Component
public class LeaveRequestMapper {

    public LeaveRequest toEntity(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }

        LeaveRequest mapped = new LeaveRequest();
        mapped.setId(leaveRequest.getId());
        mapped.setStartDate(leaveRequest.getStartDate());
        mapped.setEndDate(leaveRequest.getEndDate());
        mapped.setWorkingDay(leaveRequest.getWorkingDay());
        mapped.setReason(leaveRequest.getReason());
        mapped.setStatus(leaveRequest.getStatus());
        mapped.setManagerComment(leaveRequest.getManagerComment());
        mapped.setDecisionDate(leaveRequest.getDecisionDate());
        mapped.setEmployee(leaveRequest.getEmployee());
        mapped.setLeaveType(leaveRequest.getLeaveType());
        return mapped;
    }

    public LeaveRequest toResponse(LeaveRequest leaveRequest) {
        return toEntity(leaveRequest);
    }

    public List<LeaveRequest> toEntityList(List<LeaveRequest> leaveRequests) {
        if (leaveRequests == null) {
            return null;
        }
        return leaveRequests.stream().map(this::toEntity).toList();
    }

    public List<LeaveRequest> toResponseList(List<LeaveRequest> leaveRequests) {
        return toEntityList(leaveRequests);
    }
}
