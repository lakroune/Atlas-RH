package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    /**
     *
     * @param employee
     * @return
     */
    Employee createEmployee(Employee employee);

    /**
     *
     * @param id
     * @param updatedEmployee
     * @return
     */
    Employee updateEmployee(Long id, Employee updatedEmployee);

    /**
     *
     * @param id
     * @return
     */
    Optional<Employee> getEmployeeById(Long id);

    /**
     * @return
     */
    List<Employee> getAllEmployees();

    /**
     * @param keyword
     * @param pageable
     * @return
     */
    Page<Employee> getEmployeesWithPaginationAndSearch(String keyword, Pageable pageable);

    /**
     * @param managerId
     * @return
     */
    List<Employee> getTeamByManagerId(Long managerId);

    /**
     * @param id
     */
    void deleteEmployee(Long id);
}