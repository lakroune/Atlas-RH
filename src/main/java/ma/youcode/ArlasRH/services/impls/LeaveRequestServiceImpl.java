package ma.youcode.ArlasRH.services.impls;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.services.LeaveRequestService;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestCreateMapper leaveRequestCreateMapper;

    @Override
    public LeaveRequestRespense createLeaveRequest(Long employeeId, LeaveRequesCreatetRequest leaveRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Leave type not found."));

        LeaveRequest entity = leaveRequestCreateMapper.toEntity(leaveRequest);
        entity.setEmployee(employee);
        entity.setLeaveType(leaveType);

        if (entity.getStatus() == null) {
            entity.setStatus(LeaveStatus.EN_ATTENTE);
        }
        LeaveRequest saved = leaveRequestRepository.save(entity);
        return leaveRequestCreateMapper.toResponse(saved);
    }

    @Override
    public List<LeaveRequestRespense> getLeaveRequestsByEmployee(Long employeeId, LeaveStatus status) {
        return List.of();
    }

    @Override
    public LeaveRequestRespense cancelLeaveRequest(Long leaveRequestId, Long employeeId) {
        return null;
    }

    @Override
    public Optional<LeaveRequestRespense> getLeaveRequestById(Long id) {
        return Optional.empty();
    }
}
