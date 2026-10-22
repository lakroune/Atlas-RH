
package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.mappers.EmployeeMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeResponse createEmployee(Employee employee) {

        if (employeeRepository.existsByMatricule(employee.getMatricule())) {
            throw new RuntimeException("Employee already exists.");
        }

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            Employee updatedEmployee) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee does not exist.")
                );

        if (!employee.getMatricule().equals(updatedEmployee.getMatricule())
                && employeeRepository.existsByMatricule(
                updatedEmployee.getMatricule())) {
            throw new RuntimeException("Matricule already exists.");
        }

        if (!employee.getEmail().equals(updatedEmployee.getEmail())
                && employeeRepository.existsByEmail(
                updatedEmployee.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        employee.setMatricule(updatedEmployee.getMatricule());
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPosition(updatedEmployee.getPosition());
        employee.setDateDeRejoindre(
                updatedEmployee.getDateDeRejoindre()
        );
        employee.setRole(updatedEmployee.getRole());

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found")
                );
    }


    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeResponse> getEmployeesWithPaginationAndSearch(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public List<EmployeeResponse> getTeamByManagerId(Long managerId) {
        return List.of();
    }


    @Override
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee does not exist.");
        }

        employeeRepository.deleteById(id);
    }
}
