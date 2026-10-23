
package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyExistsException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.mappers.EmployeeMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.EmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            DepartmentRepository departmentRepository) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public EmployeeResponse createEmployee(
            Long idDepartment, Employee employee) {

        Department department = departmentRepository.findById(idDepartment)
                .orElseThrow(() -> new EmployeeNotFoundException("Department not found."));

        if (employeeRepository.existsByMatricule(employee.getMatricule())) {
            throw new EmployeeAlreadyExistsException("Matricule already exists.");
        }
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new EmployeeAlreadyExistsException("Email already exists.");
        }
        employee.setDepartement(department);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, Employee updatedEmployee) {
        return employeeMapper.toResponse(new Employee());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(
            Long idDepartment, Long idEmployee) {

        if (!departmentRepository.existsById(idDepartment)) {
            throw new EmployeeNotFoundException("Department not found.");
        }

        Employee employee = employeeRepository
                .findByIdAndDepartement_Id(idEmployee, idDepartment)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        return employeeMapper.toResponse(employee);
    }


    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getTeamByManagerId(Long managerId) {

        return employeeRepository.findByManager_Id(managerId)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteEmployee(Long idDepartment, Long idEmployee) {

        if (!departmentRepository.existsById(idDepartment)) {
            throw new DepartmentNotFoundException("Department not found.");
        }

        Employee employee = employeeRepository
                .findByIdAndDepartement_Id(idEmployee, idDepartment)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByDepartmentId(
            Long idDepartment) {

        if (!departmentRepository.existsById(idDepartment)) {
            throw new EmployeeNotFoundException("Department not found.");
        }

        return employeeRepository.findByDepartement_Id(idDepartment)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}
