package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.services.LeaveRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public LeaveRequest createLeaveRequest(Long employeeId, LeaveRequest leaveRequest) {
        return null;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployee(Long employeeId, LeaveStatus status) {
        return List.of();
    }

    @Override
    public LeaveRequest cancelLeaveRequest(Long leaveRequestId, Long employeeId) {
        return null;
    }

    @Override
    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        return Optional.empty();
    }
}
