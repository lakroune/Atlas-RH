
package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(Long idDepartment, Employee employee);

    
    EmployeeResponse getEmployeeById(Long idDepartment, Long idEmployee);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getTeamByManagerId(Long managerId);

    void deleteEmployee(Long idDepartment, Long idEmployee);

    List<EmployeeResponse> getEmployeesByDepartmentId(Long idDepartment);

    EmployeeResponse updateEmployee(Long idD, Long idE, Employee employee);
}
