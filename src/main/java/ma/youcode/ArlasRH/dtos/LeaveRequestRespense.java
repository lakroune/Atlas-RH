package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestRespense {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer workingDay;
    private String reason;
    private LeaveStatus status;
    private String managerComment;
    private LocalDate decisionDate;
    private Long employeeId;
    private Long leaveTypeId;
}
