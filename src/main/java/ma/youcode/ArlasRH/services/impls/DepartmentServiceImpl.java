
package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.exceptions.DepartmentAlreadyExistsException;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponse createDepartement(Department department) {

        if (departmentRepository.existsByName(department.getName())) {
            throw new DepartmentAlreadyExistsException(
                    "The department name already exists."
            );
        }

        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getAllDepartements() {

        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getDepartementById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toResponse)
                .orElseThrow(() ->
                        new RuntimeException("Department not found")
                );
    }

    @Override
    public DepartmentResponse updateDepartement(Long id, Department updatedDepartment) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found.")
                );
        department.setName(updatedDepartment.getName());
        department.setDescription(updatedDepartment.getDescription());
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    public void deleteDepartement(Long id) {

        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found.");
        }
        departmentRepository.deleteById(id);
    }
}
