package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);
}
