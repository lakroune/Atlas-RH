package ma.youcode.ArlasRH.services;

import java.util.List;
import java.util.Optional;

import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

public interface LeaveRequestService {
    /**
     *
     * @param employeeId
     * @param leaveRequest
     * @return
     */
    LeaveRequestRespense createLeaveRequest(Long employeeId, LeaveRequesCreatetRequest leaveRequest);

    /**
     *
     * @param employeeId
     * @param status
     * @return
     */
    List<LeaveRequestRespense> getLeaveRequestsByEmployee(Long employeeId, LeaveStatus status);

    /**
     *
     * @param leaveRequestId
     * @param employeeId
     * @return
     */
    LeaveRequestRespense cancelLeaveRequest(Long leaveRequestId, Long employeeId);

    /**
     *
     * @param id
     * @return
     */
    Optional<LeaveRequestRespense> getLeaveRequestById(Long id);

    List<LeaveRequestRespense> getAllLeaveRequest(Long idE);
}