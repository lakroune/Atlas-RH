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
        return departmentRepository.save(department); // Correction ici (minuscule)
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
        Optional<Department> departmentOptional = this.getDepartementById(id);
        if (departmentOptional.isPresent()) {
            Department existingDepartment = departmentOptional.get();
            existingDepartment.setName(updatedDepartment.getName());
            existingDepartment.setDescription(updatedDepartment.getDescription());
            existingDepartment.setEmployees(updatedDepartment.getEmployees());

            return departmentRepository.save(existingDepartment); // Correction ici (passer l'entité et retourner)
        } else {
            throw new RuntimeException("Département non trouvé avec l'id : " + id);
        }
    }

    @Override
    public void deleteDepartement(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Département non trouvé avec l'id : " + id);
        }
        departmentRepository.deleteById(id);
    }
}