package ma.youcode.ArlasRH.mappers;

import ma.youcode.ArlasRH.dtos.LeaveTypeRequest;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.models.LeaveType;

public class LeaveTypeMapper {


    public LeaveType toEntity(LeaveTypeRequest request){
        return  new LeaveType(
                request.getName(),
                request.getDaysPerYear()
        );
    }

    public static LeaveTypeResponse toResponse(LeaveType leaveType){
        return  new LeaveTypeResponse(
                leaveType.getName(),
                leaveType.getDaysPerYear()
        );
    }

}
