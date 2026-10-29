package ma.youcode.ArlasRH.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ma.youcode.ArlasRH.models.LeaveType;

@Getter
@Setter
@AllArgsConstructor
public class LeaveTypeResponse {
    private  String name;
    private  Integer daysPerYear;


}
