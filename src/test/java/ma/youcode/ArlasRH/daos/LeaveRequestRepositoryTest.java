package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeaveRequestRepositoryTest {

    @Autowired LeaveRequestRepository leaveRequestRepository;
    @Autowired LeaveTypeRepository leaveTypeRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentRepository departmentRepository;

    private Department department;
    private Employee manager;
    private Employee employee;
    private LeaveType leaveType;

    @BeforeEach
    void setUp() {
        department = departmentRepository.save(new Department("Engineering", "Product engineering"));
        manager = employeeRepository.save(employee("MGR-001", "manager@example.com", Role.MANAGER, null));
        employee = employeeRepository.saveAndFlush(employee("EMP-001", "employee@example.com", Role.EMPLOYE, manager));
        leaveType = leaveTypeRepository.saveAndFlush(new LeaveType("Annual", 22));
    }

    @Test
    void shouldFindEmployeeRequestsAndDetectTheirStatus() {
        LeaveRequest pending = leaveRequest(LeaveStatus.EN_ATTENTE, LocalDate.of(2026, 12, 1));
        LeaveRequest approved = leaveRequest(LeaveStatus.APPROUVEE, LocalDate.of(2027, 1, 5));
        leaveRequestRepository.saveAllAndFlush(java.util.List.of(pending, approved));

        assertThat(leaveRequestRepository.findByEmployee_Id(employee.getId()))
                .hasSize(2)
                .extracting(LeaveRequest::getStatus)
                .containsExactlyInAnyOrder(LeaveStatus.EN_ATTENTE, LeaveStatus.APPROUVEE);
        assertThat(leaveRequestRepository.existsByEmployee_IdAndStatus(employee.getId(), LeaveStatus.EN_ATTENTE))
                .isTrue();
        assertThat(leaveRequestRepository.existsByEmployee_IdAndStatus(employee.getId(), LeaveStatus.REFUSEE))
                .isFalse();
    }

    @Test
    void shouldFilterManagerRequestsByStatus() {
        leaveRequestRepository.saveAllAndFlush(java.util.List.of(
                leaveRequest(LeaveStatus.EN_ATTENTE, LocalDate.of(2026, 12, 1)),
                leaveRequest(LeaveStatus.REFUSEE, LocalDate.of(2027, 1, 5))));

        assertThat(leaveRequestRepository.findByEmployee_Manager_Id(manager.getId())).hasSize(2);
        assertThat(leaveRequestRepository.findByEmployee_Manager_IdAndStatus(
                manager.getId(), LeaveStatus.REFUSEE))
                .singleElement()
                .extracting(LeaveRequest::getStatus)
                .isEqualTo(LeaveStatus.REFUSEE);
    }

    @Test
    void shouldFilterRequestsByEmployeeAndDepartment() {
        leaveRequestRepository.saveAndFlush(leaveRequest(LeaveStatus.EN_ATTENTE, LocalDate.of(2026, 12, 1)));

        assertThat(leaveRequestRepository.findByEmployee_IdAndEmployee_Departement_Id(
                employee.getId(), department.getId())).hasSize(1);
        assertThat(leaveRequestRepository.findByEmployee_IdAndEmployee_Departement_Id(
                employee.getId(), 999L)).isEmpty();
    }

    private Employee employee(String matricule, String email, Role role, Employee employeeManager) {
        Employee value = new Employee();
        value.setMatricule(matricule);
        value.setFirstName(role == Role.MANAGER ? "Nadia" : "Omar");
        value.setLastName("Test");
        value.setEmail(email);
        value.setPosition(role == Role.MANAGER ? "Manager" : "Developer");
        value.setDateDeRejoindre(LocalDate.of(2025, 1, 1));
        value.setRole(role);
        value.setDepartement(department);
        value.setManager(employeeManager);
        return value;
    }

    private LeaveRequest leaveRequest(LeaveStatus status, LocalDate startDate) {
        return new LeaveRequest(
                startDate,
                startDate.plusDays(2),
                3,
                "Personal leave",
                status,
                null,
                null,
                employee,
                leaveType);
    }
}
