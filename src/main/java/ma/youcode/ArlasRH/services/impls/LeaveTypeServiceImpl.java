package ma.youcode.ArlasRH.services.impls;

import lombok.AllArgsConstructor;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.mappers.LeaveTypeMapper;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.services.LeaveTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public LeaveTypeResponse createLeaveType(LeaveType leaveType) {
        return LeaveTypeMapper.toResponse(leaveTypeRepository.save(leaveType));
    }

    @Override
    public LeaveType updateLeaveType(Long id, LeaveType updatedLeaveType) {
        return null;
    }

    @Override
    public void deleteLeaveType(Long id) {
        if (!leaveTypeRepository.existsById(id)) {
            throw new LeaveTypeNotFoundException("Leave type not found with id: " + id);
        }
        leaveTypeRepository.deleteById(id);
    }

    @Override
    public List<LeaveTypeResponse> getAllLeaveTypes() {
        return leaveTypeRepository.findAll()
                .stream().map(LeaveTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveTypeResponse getLeaveTypeById(Long id) {
        Optional<LeaveType> leaveTypeOptional = leaveTypeRepository.findById(id);
        if (leaveTypeOptional.isPresent()) {
            LeaveType leaveType = leaveTypeOptional.get();
            return LeaveTypeMapper.toResponse(leaveType);
        } else {
            throw new LeaveTypeNotFoundException("Leave type not found with id: " + id);
        }
    }
}
