package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartement(Department department) {
        if (departmentRepository.existsByName(department.getName()))
            throw new RuntimeException("The department name already exists.");
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartements() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartementById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department updateDepartement(Long id, Department updatedDepartment) {
        return new Department();
    }

    @Override
    public void deleteDepartement(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("the department not found");
        }

    }
}