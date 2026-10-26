package ma.youcode.ArlasRH.mappers;

import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.models.LeaveRequest;

@Component
public class LeaveRequestCreateMapper {

    public LeaveRequest toEntity(LeaveRequesCreatetRequest leaveRequest) {


        LeaveRequest mapped = new LeaveRequest();
        return mapped;
    }

    public LeaveRequestRespense toResponse(LeaveRequest leaveRequest) {
        return null;
    }


}
