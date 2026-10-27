
package ma.youcode.ArlasRH.services;

import java.util.List;

import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.models.Employee;

public interface EmployeeService {

    EmployeeResponse createEmployee(Long idDepartment, Employee employee);


    EmployeeResponse getEmployeeById(Long idDepartment, Long idEmployee);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getTeamByManagerId(Long managerId);

    void deleteEmployee(Long idDepartment, Long idEmployee);

    List<EmployeeResponse> getEmployeesByDepartmentId(Long idDepartment);

    EmployeeResponse updateEmployee(Long idD, Long idE, Employee employee);

    Page<EmployeeResponse> getEmployeesByDepartmentId(
            Long idD,
            String nom,
            Pageable pageable
    );

    List<EmployeeResponse> getEmployeesByManager_Id(Long managerId);

    List<EmployeeResponse> getEmployeesByDepartmentAndManagerId(Long idDepartment, Long managerId);

    void affecterEmployesAuManager(Long managerId, List<String> matricules);

    List<LeaveRequestRespense> getLeaveRequestsByManager(Long managerId);

    LeaveRequestRespense approveLeaveRequest(Long managerId, Long leaveRequestId);

    LeaveRequestRespense rejectLeaveRequest(Long managerId, Long leaveRequestId);
}
