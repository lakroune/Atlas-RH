package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.ContractRepository;
import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.ContractAlreadyTerminatedException;
import ma.youcode.ArlasRH.exceptions.ContractNotFoundException;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyHasActiveContractException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.mappers.DepartmentMapper;
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCoverageTest {

    @Mock EmployeeRepository employeeRepository;
    @Mock DepartmentRepository departmentRepository;
    @Mock LeaveRequestRepository leaveRequestRepository;
    @Mock LeaveTypeRepository leaveTypeRepository;
    @Mock ContractRepository contractRepository;
    @Mock EmployeeCreateMapper employeeMapper;
    @Mock LeaveRequestCreateMapper leaveRequestMapper;
    @Mock DepartmentMapper departmentMapper;
    @Mock ContractMapper contractMapper;

    private EmployeeServiceImpl employeeService;
    private DepartmentServiceImpl departmentService;
    private ContractServiceImpl contractService;
    private LeaveTypeServiceImpl leaveTypeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository, employeeMapper, departmentRepository,
                leaveRequestRepository, leaveTypeRepository, leaveRequestMapper);
        departmentService = new DepartmentServiceImpl(departmentRepository, departmentMapper);
        contractService = new ContractServiceImpl(contractRepository, contractMapper, employeeRepository);
        leaveTypeService = new LeaveTypeServiceImpl(leaveTypeRepository);
    }

    @Test
    void shouldReadUpdateAndDeleteEmployees() {
        Department department = new Department("IT", "Technology");
        Employee employee = new Employee();
        EmployeeResponse response = new EmployeeResponse();
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.findByIdAndDepartement_Id(2L, 1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        assertThat(employeeService.getEmployeeById(1L, 2L)).isSameAs(response);
        employeeService.deleteEmployee(1L, 2L);
        verify(employeeRepository).delete(employee);

        Employee changes = new Employee();
        changes.setPosition("Lead"); changes.setRole(Role.MANAGER);
        when(employeeRepository.save(employee)).thenReturn(employee);
        assertThat(employeeService.updateEmployee(1L, 2L, changes)).isSameAs(response);
        assertThat(employee.getPosition()).isEqualTo("Lead");
        assertThat(employee.getRole()).isEqualTo(Role.MANAGER);

        employee.setDepartement(department);
    }

    @Test
    void shouldListEmployeesUsingEveryFilter() {
        Employee employee = new Employee();
        EmployeeResponse response = new EmployeeResponse();
        when(employeeMapper.toResponse(employee)).thenReturn(response);
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeRepository.findByManager_Id(7L)).thenReturn(List.of(employee));
        when(employeeRepository.findByDepartement_Id(1L)).thenReturn(List.of(employee));
        when(employeeRepository.findByDepartement_IdAndManager_Id(1L, 7L)).thenReturn(List.of(employee));
        when(departmentRepository.existsById(1L)).thenReturn(true);

        assertThat(employeeService.getAllEmployees()).containsExactly(response);
        assertThat(employeeService.getTeamByManagerId(7L)).containsExactly(response);
        assertThat(employeeService.getEmployeesByManager_Id(7L)).containsExactly(response);
        assertThat(employeeService.getEmployeesByDepartmentId(1L)).containsExactly(response);
        assertThat(employeeService.getEmployeesByDepartmentAndManagerId(1L, 7L)).containsExactly(response);
    }

    @Test
    void shouldPageEmployeesWithAndWithoutName() {
        Employee employee = new Employee();
        EmployeeResponse response = new EmployeeResponse();
        PageRequest page = PageRequest.of(0, 10);
        when(employeeMapper.toResponse(employee)).thenReturn(response);
        when(employeeRepository.findByDepartement_Id(1L, page)).thenReturn(new PageImpl<>(List.of(employee)));
        when(employeeRepository.findByDepartement_IdAndFirstNameContainingIgnoreCase(1L, "Ada", page))
                .thenReturn(new PageImpl<>(List.of(employee)));

        assertThat(employeeService.getEmployeesByDepartmentId(1L, " ", page).getContent()).containsExactly(response);
        assertThat(employeeService.getEmployeesByDepartmentId(1L, "Ada", page).getContent()).containsExactly(response);
    }

    @Test
    void shouldReadAndRejectManagerLeaveRequests() {
        Employee manager = new Employee(); manager.setId(7L); manager.setRole(Role.MANAGER);
        Employee employee = new Employee(); employee.setManager(manager);
        LeaveRequest request = new LeaveRequest(); request.setEmployee(employee);
        LeaveRequestRespense response = new LeaveRequestRespense();
        when(employeeRepository.findById(7L)).thenReturn(Optional.of(manager));
        when(leaveRequestRepository.findByEmployee_Manager_IdAndStatus(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(List.of(request));
        when(leaveRequestRepository.findById(9L)).thenReturn(Optional.of(request));
        when(leaveRequestRepository.save(request)).thenReturn(request);
        when(leaveRequestMapper.toResponse(request)).thenReturn(response);

        assertThat(employeeService.getLeaveRequestsByManager(7L)).containsExactly(response);
        assertThat(employeeService.rejectLeaveRequest(7L, 9L)).isSameAs(response);
        assertThat(request.getStatus()).isEqualTo(ma.youcode.ArlasRH.models.enums.LeaveStatus.REFUSEE);
    }

    @Test
    void shouldCalculateSeniority() {
        Employee employee = new Employee(); employee.setDateDeRejoindre(LocalDate.now().minusYears(3).minusMonths(2));
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));

        assertThat(employeeService.getLeaveBalanceAncien(1L, 2L)).containsEntry("years", 3).containsKey("months");
    }

    @Test
    void shouldExerciseContractLifecycle() {
        Employee employee = new Employee();
        Contract contract = new Contract();
        ContractResponse response = new ContractResponse();
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(contractRepository.existsByEmployee_IdAndIsActiveTrue(2L)).thenReturn(false);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponse(contract)).thenReturn(response);

        assertThat(contractService.createContract(2L, contract)).isSameAs(response);
        assertThat(contract.getEmployee()).isSameAs(employee);

        when(employeeRepository.existsById(2L)).thenReturn(true);
        when(contractRepository.findByEmployee_Id(2L)).thenReturn(List.of(contract));
        when(contractRepository.findById(3L)).thenReturn(Optional.of(contract));
        assertThat(contractService.getContractHistoryByEmployeeId(2L)).containsExactly(response);
        assertThat(contractService.getContractById(2L, 3L)).isSameAs(response);
        assertThat(contractService.terminerContract(2L, 3L)).isSameAs(response);
        assertThat(contract.getEndDate()).isNotNull();
        assertThat(contract.getActive()).isFalse();
        assertThat(contractService.terminateContract(3L, LocalDate.now())).isNull();
    }

    @Test
    void shouldRejectInvalidContracts() {
        Contract contract = new Contract();
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> contractService.createContract(2L, contract)).isInstanceOf(RuntimeException.class);

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(new Employee()));
        when(contractRepository.existsByEmployee_IdAndIsActiveTrue(2L)).thenReturn(true);
        assertThatThrownBy(() -> contractService.createContract(2L, contract))
                .isInstanceOf(EmployeeAlreadyHasActiveContractException.class);

        when(employeeRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> contractService.getContractHistoryByEmployeeId(2L)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> contractService.getContractById(2L, 3L)).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldManageDepartments() {
        Department department = new Department("IT", "Technology"); department.setEmployees(List.of());
        Department updated = new Department("Engineering", "Product engineering");
        DepartmentResponse response = new DepartmentResponse(1L, "IT", "Technology");
        when(departmentRepository.existsByName("IT")).thenReturn(false);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toResponse(department)).thenReturn(response);
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        assertThat(departmentService.createDepartement(department)).isSameAs(response);
        assertThat(departmentService.getAllDepartements()).containsExactly(response);
        assertThat(departmentService.getDepartementById(1L)).isSameAs(response);
        assertThat(departmentService.updateDepartement(1L, updated)).isSameAs(response);
        assertThat(department.getName()).isEqualTo("Engineering");
        departmentService.deleteDepartement(1L);
        verify(departmentRepository).delete(department);
    }

    @Test
    void shouldManageLeaveTypes() {
        LeaveType leaveType = new LeaveType("Annual", 22);
        when(leaveTypeRepository.save(leaveType)).thenReturn(leaveType);
        when(leaveTypeRepository.findAll()).thenReturn(List.of(leaveType));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));
        when(leaveTypeRepository.existsById(1L)).thenReturn(true);

        assertThat(leaveTypeService.createLeaveType(leaveType).getName()).isEqualTo("Annual");
        assertThat(leaveTypeService.getAllLeaveTypes()).hasSize(1);
        assertThat(leaveTypeService.getLeaveTypeById(1L).getDaysPerYear()).isEqualTo(22);
        leaveTypeService.deleteLeaveType(1L);
        verify(leaveTypeRepository).deleteById(1L);
        assertThat(leaveTypeService.updateLeaveType(1L, leaveType)).isNull();

        when(leaveTypeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> leaveTypeService.getLeaveTypeById(99L)).isInstanceOf(LeaveTypeNotFoundException.class);
    }
}
