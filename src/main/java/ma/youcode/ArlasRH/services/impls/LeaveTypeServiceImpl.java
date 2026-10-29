package ma.youcode.ArlasRH.services.impls;

import lombok.AllArgsConstructor;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.mappers.LeaveTypeMapper;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.services.LeaveTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveTypeServiceImpl  implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;



    @Override
    public LeaveType createLeaveType(LeaveType leaveType) {
        return null;
    }

    @Override
    public LeaveType updateLeaveType(Long id, LeaveType updatedLeaveType) {
        return null;
    }

    @Override
    public void deleteLeaveType(Long id) {

    }

    @Override
    public List<LeaveTypeResponse> getAllLeaveTypes() {
        return leaveTypeRepository.findAll()
                .stream().
                map(LeaveTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeaveType> getLeaveTypeById(Long id) {
        return Optional.empty();
    }
}
