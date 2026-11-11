package ma.youcode.ArlasRH.mappers;

import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.dtos.DepartmentRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.EmployeeUpdateRequest;
import ma.youcode.ArlasRH.dtos.LeaveTypeRequest;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.ContratType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MapperAndModelTest {

    @Test
    void shouldMapContractsBothWaysAndExerciseAccessors() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        Employee employee = new Employee();
        employee.setId(7L);
        Contract contract = new Contract(ContratType.CDI, start, null, 9000.0, employee);
        contract.setId(3L);
        contract.setActive(false);
        ContractMapper mapper = new ContractMapper();

        ContractResponse response = mapper.toResponse(contract);
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getEmployeeId()).isEqualTo(7L);
        assertThat(response.getType()).isEqualTo(ContratType.CDI);
        assertThat(contract.getStartDate()).isEqualTo(start);
        assertThat(contract.getEndDate()).isNull();
        assertThat(contract.getGrossSalary()).isEqualTo(9000.0);
        assertThat(contract.getEmployee()).isSameAs(employee);
        assertThat(contract.getActive()).isFalse();

        ContractCreateRequest request = new ContractCreateRequest(ContratType.CDD, start, start.plusMonths(6), 5000.0);
        Contract mapped = mapper.toEntity(request);
        assertThat(mapped.getType()).isEqualTo(ContratType.CDD);
        assertThat(mapped.getEndDate()).isEqualTo(start.plusMonths(6));
        assertThat(mapped.getGrossSalary()).isEqualTo(5000.0);
    }

    @Test
    void shouldMapDepartmentsAndExerciseAccessors() {
        DepartmentMapper mapper = new DepartmentMapper();
        Department department = mapper.toEntity(new DepartmentRequest("Finance", "Finance team"));
        department.setId(4L);
        department.setName("Accounting");
        department.setDescription("Accounting team");
        department.setEmployees(List.of(new Employee()));

        assertThat(mapper.toResponse(department).id()).isEqualTo(4L);
        assertThat(department.getName()).isEqualTo("Accounting");
        assertThat(department.getDescription()).isEqualTo("Accounting team");
        assertThat(department.getEmployees()).hasSize(1);
        assertThat(new DepartmentMapper.ContractMapper()).isNotNull();
    }

    @Test
    void shouldMapEmployeeUpdateAndExerciseEmployeeAccessors() {
        Department department = new Department("IT", "Technology");
        department.setId(8L);
        Employee manager = new Employee();
        Employee employee = new Employee("M-1", "Ada", "Lovelace", "ada@test.com", "Dev",
                LocalDate.of(2020, 1, 1), Role.EMPLOYE, department, List.of(), List.of(), List.of(), manager);
        employee.setId(2L);
        EmployeeUpdateMapper mapper = new EmployeeUpdateMapper();

        EmployeeResponse response = mapper.toResponse(employee);
        assertThat(response.getId()).isEqualTo(2L);
        Employee updated = mapper.toEntity(new EmployeeUpdateRequest("Lead", Role.MANAGER));
        assertThat(updated.getPosition()).isEqualTo("Lead");
        assertThat(updated.getRole()).isEqualTo(Role.MANAGER);

        employee.setMatricule("M-2"); employee.setFirstName("Grace"); employee.setLastName("Hopper");
        employee.setEmail("grace@test.com"); employee.setPosition("Lead"); employee.setDateDeRejoindre(LocalDate.now());
        employee.setRole(Role.MANAGER); employee.setDepartement(department); employee.setManager(manager);
        employee.setLeaveRequest(List.of(new LeaveRequest())); employee.setContracts(List.of(new Contract()));
        employee.setEmployees(List.of(new Employee()));
        assertThat(employee.getMatricule()).isEqualTo("M-2");
        assertThat(employee.getFirstName()).isEqualTo("Grace");
        assertThat(employee.getLastName()).isEqualTo("Hopper");
        assertThat(employee.getEmail()).isEqualTo("grace@test.com");
        assertThat(employee.getLeaveRequest()).hasSize(1);
        assertThat(employee.getContracts()).hasSize(1);
        assertThat(employee.getEmployees()).hasSize(1);
        assertThat(employee.getManager()).isSameAs(manager);
    }

    @Test
    void shouldMapLeaveTypesAndExerciseLeaveRequest() {
        LeaveType leaveType = LeaveTypeMapper.toEntity(new LeaveTypeRequest("Annual", 22));
        leaveType.setId(5L);
        leaveType.setLeaveRequestList(List.of());
        assertThat(LeaveTypeMapper.toResponse(leaveType).getDaysPerYear()).isEqualTo(22);
        assertThat(leaveType.getId()).isEqualTo(5L);
        assertThat(leaveType.getLeaveRequestList()).isEmpty();

        LocalDate start = LocalDate.of(2026, 5, 1);
        Employee employee = new Employee();
        LeaveRequest request = new LeaveRequest(start, start.plusDays(2), 2, "Rest", LeaveStatus.EN_ATTENTE,
                "pending", null, employee, leaveType);
        request.setId(9L); request.setStatus(LeaveStatus.APPROUVEE); request.setManagerComment("ok");
        request.setDecisionDate(start); request.setWorkingDay(2); request.setReason("Holiday");
        assertThat(request.getId()).isEqualTo(9L);
        assertThat(request.getStartDate()).isEqualTo(start);
        assertThat(request.getEndDate()).isEqualTo(start.plusDays(2));
        assertThat(request.getStatus()).isEqualTo(LeaveStatus.APPROUVEE);
        assertThat(request.getManagerComment()).isEqualTo("ok");
        assertThat(request.getDecisionDate()).isEqualTo(start);
        assertThat(request.getWorkingDay()).isEqualTo(2);
        assertThat(request.getReason()).isEqualTo("Holiday");
        assertThat(request.getEmployee()).isSameAs(employee);
        assertThat(request.getLeaveType()).isSameAs(leaveType);
    }
}
