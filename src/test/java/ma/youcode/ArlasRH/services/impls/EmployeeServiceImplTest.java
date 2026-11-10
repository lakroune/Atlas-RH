package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.LeaveRequestAprouveRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyExistsException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.ManagerNotFoundException;
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.LeaveRequestCreateMapper;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

        @Mock
        private EmployeeRepository employeeRepository;

        @Mock
        private EmployeeCreateMapper employeeCreateMapper;

        @Mock
        private DepartmentRepository departmentRepository;

        @Mock
        private LeaveRequestRepository leaveRequestRepository;

        @Mock
        private LeaveTypeRepository leaveTypeRepository;

        @Mock
        private LeaveRequestCreateMapper leaveRequestCreateMapper;

        @InjectMocks
        private EmployeeServiceImpl employeeService;

        @Test
        @Description("Test de la création d'un employé avec un matricule déjà existant")
        void testEmployeeCreationAvecMatriculeExistant() {

                Long departmentId = 1L;
                Department department = new Department("IT", "Information Technology");
                Employee employee = new Employee();

                employee.setMatricule("EMP-001");
                employee.setEmail("employee@gmail.com");

                when(departmentRepository.findById(departmentId))
                                .thenReturn(Optional.of(department));

                when(employeeRepository.existsByMatricule("EMP-001"))
                                .thenReturn(true);

                assertThrows(
                                EmployeeAlreadyExistsException.class,
                                () -> employeeService.createEmployee(departmentId, employee));

                verify(employeeRepository, never()).save(employee);
        }

        @Test
        @Description("Test de la création d'un employé avec un matricule non existant")
        void testEmployeeCreationAvecMatriculeNonExistant() {

                Long departmentId = 1L;
                Department department = new Department("IT", "Information Technology");
                Employee employee = new Employee();

                employee.setMatricule("EMP-001");
                employee.setEmail("employee@gmail.com");

                when(departmentRepository.findById(departmentId))
                                .thenReturn(Optional.of(department));

                when(employeeRepository.existsByMatricule("EMP-001"))
                                .thenReturn(false);

                when(employeeRepository.existsByEmail("employee@gmail.com"))
                                .thenReturn(false);

                when(employeeRepository.save(employee))
                                .thenReturn(employee);

                EmployeeResponse expectedResponse = new EmployeeResponse();
                when(employeeCreateMapper.toResponse(employee))
                                .thenReturn(expectedResponse);

                EmployeeResponse actualResponse = employeeService.createEmployee(departmentId, employee);

                assertSame(expectedResponse, actualResponse);
                assertSame(department, employee.getDepartement());
                verify(employeeRepository).save(employee);
                verify(employeeCreateMapper).toResponse(employee);

        }

        @Test
        @Description("Test de la création d'un employé avec un email déjà existant")
        void testEmployeeCreationAvecEmailExistant() {
                Long departmentId = 1L;
                Department department = new Department("IT", "Information Technology");
                Employee employee = new Employee();

                employee.setMatricule("EMP-002");
                employee.setEmail("employee@gmail.com");

                when(departmentRepository.findById(departmentId))
                                .thenReturn(Optional.of(department));
                when(employeeRepository.existsByMatricule("EMP-002"))
                                .thenReturn(false);
                when(employeeRepository.existsByEmail("employee@gmail.com"))
                                .thenReturn(true);

                assertThrows(
                                EmployeeAlreadyExistsException.class,
                                () -> employeeService.createEmployee(departmentId, employee));

                verify(employeeRepository, never()).save(employee);
        }

        @Test
        @Description("Test de la création d'un employé avec un manager qui est lui-même")
        void testManagerSoiMeme() {
                Long managerId = 1L;
                Employee manager = new Employee();
                manager.setId(managerId);
                manager.setMatricule("MGR-001");
                manager.setRole(Role.MANAGER);
                List<String> matricules = List.of("MGR-001");

                when(employeeRepository.findById(managerId))
                                .thenReturn(Optional.of(manager));
                when(employeeRepository.findAllByMatriculeIn(matricules))
                                .thenReturn(List.of(manager));

                assertThrows(
                                EmployeeNotFoundException.class,
                                () -> employeeService.affecterEmployesAuManager(managerId, matricules));

                verify(employeeRepository, never()).saveAll(List.of(manager));
        }

        @Test
        @Description("Test de la création d'un employé avec un manager qui n'existe pas")
        void testManagerInexistant() {
                Long managerId = 1L;
                List<String> matricules = List.of("EMP-001", "EMP-002");

                when(employeeRepository.findById(managerId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ManagerNotFoundException.class,
                                () -> employeeService.affecterEmployesAuManager(managerId, matricules));

        }

        @Test
        @Description("Affecter des employés à un manager")
        void testAffecterEmployesAuManagerAvecSucces() {
                Long managerId = 1L;
                Employee manager = new Employee();
                manager.setId(managerId);
                manager.setRole(Role.MANAGER);
                Employee employee = new Employee();
                employee.setRole(Role.EMPLOYE);
                List<String> matricules = List.of("EMP-001");

                when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
                when(employeeRepository.findAllByMatriculeIn(matricules))
                                .thenReturn(List.of(employee));

                employeeService.affecterEmployesAuManager(managerId, matricules);

                assertSame(manager, employee.getManager());
                verify(employeeRepository).saveAll(List.of(employee));
        }

        @Test
        @Description("Calculer le solde annuel restant")
        void testSoldeCongesAvecSucces() {
                Long departmentId = 1L;
                Long employeeId = 2L;
                Department department = new Department("IT", "Information Technology");
                department.setId(departmentId);
                Employee employee = new Employee();
                employee.setDepartement(department);
                LeaveType leaveType = new LeaveType("Congé annuel", 25);
                leaveType.setId(10L);
                LeaveRequest usedLeave = new LeaveRequest();
                usedLeave.setStatus(LeaveStatus.APPROUVEE);
                usedLeave.setLeaveType(leaveType);
                usedLeave.setStartDate(LocalDate.now().minusMonths(1));
                usedLeave.setWorkingDay(5);

                when(departmentRepository.existsById(departmentId)).thenReturn(true);
                when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
                when(leaveRequestRepository.findByEmployee_IdAndEmployee_Departement_Id(employeeId, departmentId))
                                .thenReturn(List.of(usedLeave));
                when(leaveTypeRepository.findAll()).thenReturn(List.of(leaveType));

                List<String> balances = employeeService.getLeaveBalance(departmentId, employeeId);

                assertEquals(List.of("Congé annuel: 20 jours restants"), balances);
        }

        @Test
        @Description("Refuser le calcul du solde pour un département inexistant")
        void testSoldeCongesDepartementInexistant() {
                when(departmentRepository.existsById(99L)).thenReturn(false);

                assertThrows(
                                DepartmentNotFoundException.class,
                                () -> employeeService.getLeaveBalance(99L, 2L)
                );
        }

        @Test
        @Description("Approuver une demande de congé")
        void testApprouverDemandeAvecSucces() {
                Long managerId = 1L;
                Long requestId = 10L;
                Employee manager = new Employee();
                manager.setId(managerId);
                manager.setRole(Role.MANAGER);
                Employee employee = new Employee();
                employee.setManager(manager);
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setEmployee(employee);
                leaveRequest.setStatus(LeaveStatus.EN_ATTENTE);
                LeaveRequestAprouveRequest decision =
                                new LeaveRequestAprouveRequest("Demande validée");
                LeaveRequestRespense expectedResponse = new LeaveRequestRespense();

                when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
                when(leaveRequestRepository.findById(requestId)).thenReturn(Optional.of(leaveRequest));
                when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);
                when(leaveRequestCreateMapper.toResponse(leaveRequest)).thenReturn(expectedResponse);

                LeaveRequestRespense actual =
                                employeeService.approveLeaveRequest(managerId, requestId, decision);

                assertSame(expectedResponse, actual);
                assertEquals(LeaveStatus.APPROUVEE, leaveRequest.getStatus());
                assertEquals("Demande validée", leaveRequest.getManagerComment());
                assertNotNull(leaveRequest.getDecisionDate());
                verify(leaveRequestRepository).save(leaveRequest);
        }

        @Test
        @Description("Refuser une décision prise par un non-manager")
        void testApprouverDemandeParNonManager() {
                Employee employee = new Employee();
                employee.setRole(Role.EMPLOYE);
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

                assertThrows(
                                ManagerNotFoundException.class,
                                () -> employeeService.approveLeaveRequest(
                                                1L,
                                                10L,
                                                new LeaveRequestAprouveRequest("OK")
                                )
                );
        }

}
