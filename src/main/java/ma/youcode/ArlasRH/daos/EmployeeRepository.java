package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByMatricule(String matricule);

    boolean existsByEmail(String email);

    List<Employee> findByDepartement_Id(Long id);

    Optional<Employee> findByIdAndDepartement_Id(Long idEmployee, Long idDepartment);

    List<Employee> findByManager_Id(Long managerId);
}
