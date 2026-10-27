package ma.youcode.ArlasRH.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.ArlasRH.models.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_Id(Long employeeId);

    List<LeaveRequest> findByEmployee_Manager_Id(Long managerId);
}
