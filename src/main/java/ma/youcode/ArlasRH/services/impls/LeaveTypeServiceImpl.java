package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.services.LeaveTypeService;

import java.util.List;
import java.util.Optional;

public class LeaveTypeServiceImpl  implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

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
    public List<LeaveType> getAllLeaveTypes() {
        return List.of();
    }

    @Override
    public Optional<LeaveType> getLeaveTypeById(Long id) {
        return Optional.empty();
    }
}
