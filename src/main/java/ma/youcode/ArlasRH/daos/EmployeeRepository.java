package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDAO extends JpaRepository<Employee, Long> {


}
