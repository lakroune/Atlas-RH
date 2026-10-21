package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    boolean existsByMatricule(String matricule);

    boolean existsByEmail(String email);
}
