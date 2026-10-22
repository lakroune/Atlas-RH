
package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
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
    EmployeeResponse createEmployee(Employee employee);

    /**
     *
     * @param id
     * @param employee
     * @return
     */
    EmployeeResponse updateEmployee(Long id, Employee employee);

    /**
     *
     * @param id
     * @return
     */
    EmployeeResponse getEmployeeById(Long id);

    /**
     *
     * @return
     */
    List<EmployeeResponse> getAllEmployees();

    /**
     *
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EmployeeResponse> getEmployeesWithPaginationAndSearch(String keyword, Pageable pageable);

    /**
     *
     * @param managerId
     * @return
     */
    List<EmployeeResponse> getTeamByManagerId(Long managerId);

    /**
     *
     * @param id
     */
    void deleteEmployee(Long id);
}
