package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestDAO extends JpaRepository<LeaveRequest, Long> {
}
