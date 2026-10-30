package ma.youcode.ArlasRH.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_Id(Long employeeId);

    List<LeaveRequest> findByEmployee_Manager_Id(Long managerId);

    boolean existsByEmployee_IdAndStatus(Long employeeId, LeaveStatus status);

    List<LeaveRequest> findByEmployee_Manager_IdAndStatus(Long managerId, LeaveStatus leaveStatus);
}
