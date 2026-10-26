package ma.youcode.ArlasRH.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.ArlasRH.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByMatricule(String matricule);

    boolean existsByEmail(String email);

    List<Employee> findByDepartement_Id(Long id);

    Optional<Employee> findByIdAndDepartement_Id(Long idEmployee, Long idDepartment);

    List<Employee> findByManager_Id(Long managerId);

    List<Employee> findByDepartement_IdAndManager_Id(Long idDepartment, Long managerId);

    Page<Employee> findByDepartement_Id(Long idD, Pageable pageable);

    Page<Employee> findByDepartement_IdAndFirstNameContainingIgnoreCase(Long idD, String nom, Pageable pageable);

    Page<Employee> findByDepartement_IdAndLastNameContainingIgnoreCase(
            Long idD,
            String lastName,
            Pageable pageable
    );
}
