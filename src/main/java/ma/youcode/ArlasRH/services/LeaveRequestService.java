package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {
    /**
     *
     * @param employeeId
     * @param leaveRequest
     * @return
     */
    LeaveRequest createLeaveRequest(Long employeeId, LeaveRequest leaveRequest);

    /**
     *
     * @param employeeId
     * @param status
     * @return
     */
    List<LeaveRequest> getLeaveRequestsByEmployee(Long employeeId, LeaveStatus status);

    /**
     *
     * @param leaveRequestId
     * @param employeeId
     * @return
     */
    LeaveRequest cancelLeaveRequest(Long leaveRequestId, Long employeeId);

    /**
     *
     * @param id
     * @return
     */
    Optional<LeaveRequest> getLeaveRequestById(Long id);
}