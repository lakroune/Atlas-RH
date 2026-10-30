
package ma.youcode.ArlasRH.services.impls;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.youcode.ArlasRH.dtos.LeaveRequestAprouveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyExistsException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.LeaveRequestNotFoundException;
import ma.youcode.ArlasRH.exceptions.ManagerNotFoundException;
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;
import ma.youcode.ArlasRH.services.EmployeeService;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCreateMapper employeeCreateMapper;
    private final DepartmentRepository departmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestCreateMapper leaveRequestCreateMapper;

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
        return employeeCreateMapper.toResponse(savedEmployee);
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

        return employeeCreateMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employeeCreateMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getTeamByManagerId(Long managerId) {

        return employeeRepository.findByManager_Id(managerId)
                .stream()
                .map(employeeCreateMapper::toResponse)
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
                .map(employeeCreateMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse updateEmployee(Long idD, Long idE, Employee employee) {
        if (!departmentRepository.existsById(idD)) {
            throw new DepartmentNotFoundException("Department not found.");
        }

        Employee existingEmployee = employeeRepository
                .findByIdAndDepartement_Id(idE, idD)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        existingEmployee.setRole(employee.getRole());
        existingEmployee.setPosition(employee.getPosition());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeCreateMapper.toResponse(updatedEmployee);
    }

    @Override
    public Page<EmployeeResponse> getEmployeesByDepartmentId(
            Long idD,
            String nom,
            Pageable pageable) {

        Page<Employee> employees;

        if (nom == null || nom.isBlank()) {
            employees = employeeRepository
                    .findByDepartement_Id(idD, pageable);
        } else {
            employees = employeeRepository
                    .findByDepartement_IdAndFirstNameContainingIgnoreCase(
                            idD, nom, pageable);
        }

        return employees.map(employeeCreateMapper::toResponse);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByManager_Id(Long managerId) {
        return employeeRepository.findByManager_Id(managerId)
                .stream()
                .map(employeeCreateMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartmentAndManagerId(Long idDepartment, Long managerId) {
        return employeeRepository.findByDepartement_IdAndManager_Id(idDepartment, managerId)
                .stream()
                .map(employeeCreateMapper::toResponse)
                .toList();
    }

    @Override
    public void affecterEmployesAuManager(Long managerId, List<String> matricules) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));

        if (!manager.getRole().name().equalsIgnoreCase("MANAGER")) {
            throw new ManagerNotFoundException("The employee with ID " + managerId + " is not a manager.");
        }

        List<Employee> employees = employeeRepository.findAllByMatriculeIn(matricules);

        List<Employee> nonEmployeeStaff = employees.stream()
                .filter(e -> !e.getRole().name().equalsIgnoreCase("EMPLOYE"))
                .collect(Collectors.toList());

        if (!nonEmployeeStaff.isEmpty()) {
            throw new EmployeeNotFoundException("Only employees can be assigned to a manager.");
        }

        if (employees.size() != matricules.size()) {
            throw new EmployeeNotFoundException("One or more employees not found.");
        }

        for (Employee employee : employees) {
            employee.setManager(manager);
        }

        employeeRepository.saveAll(employees);
    }

    @Override
    public List<LeaveRequestRespense> getLeaveRequestsByManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));

        if (manager.getRole() != Role.MANAGER) {
            throw new ManagerNotFoundException("The employee with ID " + managerId + " is not a manager.");
        }

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployee_Manager_IdAndStatus(managerId,
                LeaveStatus.EN_ATTENTE);

        return leaveRequests.stream()
                .map(leaveRequestCreateMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveRequestRespense approveLeaveRequest(Long managerId, Long leaveRequestId,
            LeaveRequestAprouveRequest leaveRequestAprouveRequest) {

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));

        if (manager.getRole() != Role.MANAGER) {
            throw new ManagerNotFoundException("The employee with ID " + managerId + " is not a manager.");
        }

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found."));

        if (leaveRequest.getEmployee() == null
                || leaveRequest.getEmployee().getManager() == null
                || !leaveRequest.getEmployee().getManager().getId().equals(managerId)) {
            throw new ManagerNotFoundException("The leave request does not belong to the manager's team.");
        }
        if (leaveRequest.getStatus() != LeaveStatus.EN_ATTENTE) {
            throw new LeaveRequestNotFoundException("demande already aprouved.");
        }
        Employee employee = leaveRequest.getEmployee();

        leaveRequest.setManagerComment(leaveRequestAprouveRequest.getManagerComment());
        leaveRequest.setDecisionDate(LocalDate.now());
        leaveRequest.setStatus(LeaveStatus.APPROUVEE);
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);

        return leaveRequestCreateMapper.toResponse(updatedRequest);
    }

    @Override
    public LeaveRequestRespense rejectLeaveRequest(Long managerId, Long leaveRequestId) {

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));

        if (manager.getRole() != Role.MANAGER) {
            throw new ManagerNotFoundException("The employee with ID " + managerId + " is not a manager.");
        }

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found."));

        if (leaveRequest.getEmployee() == null
                || leaveRequest.getEmployee().getManager() == null
                || !leaveRequest.getEmployee().getManager().getId().equals(managerId)) {
            throw new ManagerNotFoundException("The leave request does not belong to the manager's team.");
        }

        leaveRequest.setStatus(LeaveStatus.REFUSEE);
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);

        return leaveRequestCreateMapper.toResponse(updatedRequest);
    }






    @Override
    public List<String> getLeaveBalance(Long idD, Long idE) {

        if (!departmentRepository.existsById(idD)) {
            throw new DepartmentNotFoundException("Department not found.");
        }

        Employee employee = employeeRepository.findById(idE)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        if (!employee.getDepartement().getId().equals(idD)) {
            throw new EmployeeNotFoundException(
                    "Employee does not belong to this department."
            );
        }

        int currentYear = LocalDate.now().getYear();

        List<LeaveRequest> leaveRequests =
                leaveRequestRepository.findByEmployee_IdAndEmployee_Departement_Id(idE, idD);
        List<String> leaveBalances = new ArrayList<>();

        for (LeaveType leaveType : leaveTypeRepository.findAll()) {
            int usedDays = 0;

            for (LeaveRequest leaveRequest : leaveRequests) {
                boolean isApproved = leaveRequest.getStatus() == LeaveStatus.APPROUVEE;
                boolean hasSameType = leaveRequest.getLeaveType().getId()
                        .equals(leaveType.getId());
                boolean isSameYear = leaveRequest.getStartDate().getYear() == currentYear;

                if (isApproved && hasSameType && isSameYear) {
                    usedDays += leaveRequest.getWorkingDay();
                }
            }

            int remainingDays = Math.max(
                    0,
                    leaveType.getDaysPerYear() - usedDays
            );

            leaveBalances.add(
                    leaveType.getName() + ": " + remainingDays + " jours restants"
            );
        }

        return leaveBalances;
    }



    @Override
    public List<String> getLeaveBalanceAncien(Long idD, Long idE) {
        if (!departmentRepository.existsById(idD)) {
            throw new DepartmentNotFoundException("Department not found.");
        }

        Employee employee = employeeRepository.findById(idE)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found."));

        Period anciennete = Period.between(employee.getDateDeRejoindre(), LocalDate.now());

        return List.of(
                String.valueOf(anciennete.getYears()),
                String.valueOf(anciennete.getMonths()),
                String.valueOf(anciennete.getDays()));
    }

}
