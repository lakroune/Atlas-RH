package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.models.LeaveType;

import java.util.List;
import java.util.Optional;

public interface LeaveTypeService {
    /**
     *
     * @param leaveType
     * @return
     */
    LeaveTypeResponse createLeaveType(LeaveType leaveType);

    /**
     *
     * @param id
     * @param updatedLeaveType
     * @return
     */
    LeaveType updateLeaveType(Long id, LeaveType updatedLeaveType);

    /**
     *
     * @param id
     */
    void deleteLeaveType(Long id);

    /**
     *
     * @return Array
     */
    List<LeaveTypeResponse> getAllLeaveTypes();

    /**
     *
     * @param id
     * @return
     */
    Optional<LeaveType> getLeaveTypeById(Long id);

}
