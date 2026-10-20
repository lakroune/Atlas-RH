package ma.youcode.ArlasRH.services.impls;


import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartementService;
import org.springframework.stereotype.Service;


@Service
public class DepartementServiceImpl implements DepartementService {
    private final DepartmentRepository departmentRepository;

    public DepartementServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department saveDepartement(Department department) {
        return departmentRepository.save(department);

    }
