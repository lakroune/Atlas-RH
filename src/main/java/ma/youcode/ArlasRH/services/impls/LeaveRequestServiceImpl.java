package ma.youcode.ArlasRH.services.impls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import ma.youcode.ArlasRH.exceptions.*;
import org.springframework.stereotype.Service;

import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
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
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found.");
        }
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found."));
        if(leaveRequest.getStatus() == LeaveStatus.APPROUVEE || leaveRequest.getStatus() == LeaveStatus.REFUSEE) {
            throw new LeaveRequestCannotBeCancelledException("Cannot cancel an approved or refused leave request.");
        }
        if( leaveRequest.getStatus() == LeaveStatus.ANNULEE) {
            throw new LeaveRequestAlreadyCancelledException("Leave request is already cancelled.");
        }
        leaveRequest.setStatus(LeaveStatus.ANNULEE);
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return leaveRequestCreateMapper.toResponse(updated);
    }

    @Override
    public Optional<LeaveRequestRespense> getLeaveRequestById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<LeaveRequestRespense> getAllLeaveRequest(Long idE) {
        if (!employeeRepository.existsById(idE)) {
            throw new EmployeeNotFoundException("Employee not found.");
        }

        return leaveRequestRepository.findByEmployee_Id(idE)
                .stream()
                .map(leaveRequestCreateMapper::toResponse)
                .collect(Collectors.toList());
    }
}
