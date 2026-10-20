package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return null;
    }

    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        return null;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return List.of();
    }

    @Override
    public Page<Employee> getEmployeesWithPaginationAndSearch(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public List<Employee> getTeamByManagerId(Long managerId) {
        return List.of();
    }

    @Override
    public void deleteEmployee(Long id) {

    }
}
