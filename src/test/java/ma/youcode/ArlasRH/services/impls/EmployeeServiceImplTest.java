package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
import ma.youcode.ArlasRH.exceptions.LeaveRequestNotFoundException;
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

        @Test
        @Description("Refuser la création lorsque le département est introuvable")
        void testCreateEmployeeDepartmentNotFound() {
                when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(EmployeeNotFoundException.class,
                                () -> employeeService.createEmployee(99L, new Employee()));
                verify(employeeRepository, never()).save(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @Description("Récupérer un employé dans son département")
        void testGetEmployeeById() {
                Employee employee = new Employee();
                EmployeeResponse response = new EmployeeResponse();
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findByIdAndDepartement_Id(2L, 1L)).thenReturn(Optional.of(employee));
                when(employeeCreateMapper.toResponse(employee)).thenReturn(response);

                assertSame(response, employeeService.getEmployeeById(1L, 2L));
        }

        @Test
        @Description("Refuser la recherche dans un département inexistant")
        void testGetEmployeeByIdDepartmentNotFound() {
                when(departmentRepository.existsById(99L)).thenReturn(false);

                assertThrows(EmployeeNotFoundException.class,
                                () -> employeeService.getEmployeeById(99L, 2L));
        }

        @Test
        @Description("Refuser la recherche d'un employé inexistant")
        void testGetEmployeeByIdNotFound() {
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findByIdAndDepartement_Id(99L, 1L)).thenReturn(Optional.empty());

                assertThrows(EmployeeNotFoundException.class,
                                () -> employeeService.getEmployeeById(1L, 99L));
        }

        @Test
        @Description("Lister les employés avec tous les filtres simples")
        void testEmployeeLists() {
                Employee employee = new Employee();
                EmployeeResponse response = new EmployeeResponse();
                when(employeeCreateMapper.toResponse(employee)).thenReturn(response);
                when(employeeRepository.findAll()).thenReturn(List.of(employee));
                when(employeeRepository.findByManager_Id(7L)).thenReturn(List.of(employee));
                when(employeeRepository.findByDepartement_Id(1L)).thenReturn(List.of(employee));
                when(employeeRepository.findByDepartement_IdAndManager_Id(1L, 7L)).thenReturn(List.of(employee));
                when(departmentRepository.existsById(1L)).thenReturn(true);

                assertEquals(List.of(response), employeeService.getAllEmployees());
                assertEquals(List.of(response), employeeService.getTeamByManagerId(7L));
                assertEquals(List.of(response), employeeService.getEmployeesByManager_Id(7L));
                assertEquals(List.of(response), employeeService.getEmployeesByDepartmentId(1L));
                assertEquals(List.of(response), employeeService.getEmployeesByDepartmentAndManagerId(1L, 7L));
        }

        @Test
        @Description("Paginer les employés avec et sans recherche par nom")
        void testEmployeePaginationAndNameFilter() {
                Employee employee = new Employee();
                EmployeeResponse response = new EmployeeResponse();
                PageRequest pageable = PageRequest.of(0, 10);
                when(employeeCreateMapper.toResponse(employee)).thenReturn(response);
                when(employeeRepository.findByDepartement_Id(1L, pageable))
                                .thenReturn(new PageImpl<>(List.of(employee)));
                when(employeeRepository.findByDepartement_IdAndFirstNameContainingIgnoreCase(1L, "Sara", pageable))
                                .thenReturn(new PageImpl<>(List.of(employee)));

                assertEquals(List.of(response),
                                employeeService.getEmployeesByDepartmentId(1L, " ", pageable).getContent());
                assertEquals(List.of(response),
                                employeeService.getEmployeesByDepartmentId(1L, "Sara", pageable).getContent());
        }

        @Test
        @Description("Supprimer un employé de son département")
        void testDeleteEmployee() {
                Employee employee = new Employee();
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findByIdAndDepartement_Id(2L, 1L)).thenReturn(Optional.of(employee));

                employeeService.deleteEmployee(1L, 2L);

                verify(employeeRepository).delete(employee);
        }

        @Test
        @Description("Mettre à jour le rôle et le poste d'un employé")
        void testUpdateEmployee() {
                Employee employee = new Employee();
                Employee changes = new Employee();
                changes.setRole(Role.MANAGER);
                changes.setPosition("Team lead");
                EmployeeResponse response = new EmployeeResponse();
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findByIdAndDepartement_Id(2L, 1L)).thenReturn(Optional.of(employee));
                when(employeeRepository.save(employee)).thenReturn(employee);
                when(employeeCreateMapper.toResponse(employee)).thenReturn(response);

                assertSame(response, employeeService.updateEmployee(1L, 2L, changes));
                assertEquals(Role.MANAGER, employee.getRole());
                assertEquals("Team lead", employee.getPosition());
        }

        @Test
        @Description("Refuser l'affectation lorsque la cible n'est pas manager")
        void testAssignEmployeesToNonManager() {
                Employee employee = new Employee();
                employee.setRole(Role.EMPLOYE);
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

                assertThrows(ManagerNotFoundException.class,
                                () -> employeeService.affecterEmployesAuManager(1L, List.of("EMP-001")));
        }

        @Test
        @Description("Refuser l'affectation lorsqu'un matricule est introuvable")
        void testAssignEmployeesWithMissingMatricule() {
                Employee manager = new Employee();
                manager.setRole(Role.MANAGER);
                Employee employee = new Employee();
                employee.setRole(Role.EMPLOYE);
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
                when(employeeRepository.findAllByMatriculeIn(List.of("EMP-001", "EMP-002")))
                                .thenReturn(List.of(employee));

                assertThrows(EmployeeNotFoundException.class,
                                () -> employeeService.affecterEmployesAuManager(
                                                1L, List.of("EMP-001", "EMP-002")));
        }

        @Test
        @Description("Lister les demandes en attente de l'équipe du manager")
        void testGetLeaveRequestsByManager() {
                Employee manager = new Employee();
                manager.setRole(Role.MANAGER);
                LeaveRequest request = new LeaveRequest();
                LeaveRequestRespense response = new LeaveRequestRespense();
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
                when(leaveRequestRepository.findByEmployee_Manager_IdAndStatus(1L, LeaveStatus.EN_ATTENTE))
                                .thenReturn(List.of(request));
                when(leaveRequestCreateMapper.toResponse(request)).thenReturn(response);

                assertEquals(List.of(response), employeeService.getLeaveRequestsByManager(1L));
        }

        @Test
        @Description("Refuser les demandes d'équipe pour un manager inexistant")
        void testGetLeaveRequestsManagerNotFound() {
                when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(ManagerNotFoundException.class,
                                () -> employeeService.getLeaveRequestsByManager(99L));
        }

        @Test
        @Description("Refuser l'approbation d'une demande inexistante")
        void testApproveLeaveRequestNotFound() {
                Employee manager = new Employee();
                manager.setRole(Role.MANAGER);
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
                when(leaveRequestRepository.findById(99L)).thenReturn(Optional.empty());

                assertThrows(LeaveRequestNotFoundException.class,
                                () -> employeeService.approveLeaveRequest(
                                                1L, 99L, new LeaveRequestAprouveRequest("OK")));
        }

        @Test
        @Description("Refuser une demande de congé de l'équipe")
        void testRejectLeaveRequest() {
                Employee manager = new Employee();
                manager.setId(1L);
                manager.setRole(Role.MANAGER);
                Employee employee = new Employee();
                employee.setManager(manager);
                LeaveRequest request = new LeaveRequest();
                request.setEmployee(employee);
                request.setStatus(LeaveStatus.EN_ATTENTE);
                LeaveRequestRespense response = new LeaveRequestRespense();
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
                when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(request));
                when(leaveRequestRepository.save(request)).thenReturn(request);
                when(leaveRequestCreateMapper.toResponse(request)).thenReturn(response);

                assertSame(response, employeeService.rejectLeaveRequest(1L, 10L));
                assertEquals(LeaveStatus.REFUSEE, request.getStatus());
        }

        @Test
        @Description("Refuser une décision sur la demande d'une autre équipe")
        void testRejectLeaveRequestOutsideManagerTeam() {
                Employee manager = new Employee();
                manager.setId(1L);
                manager.setRole(Role.MANAGER);
                Employee otherManager = new Employee();
                otherManager.setId(2L);
                Employee employee = new Employee();
                employee.setManager(otherManager);
                LeaveRequest request = new LeaveRequest();
                request.setEmployee(employee);
                when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
                when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(request));

                assertThrows(ManagerNotFoundException.class,
                                () -> employeeService.rejectLeaveRequest(1L, 10L));
        }

        @Test
        @Description("Refuser le solde d'un employé appartenant à un autre département")
        void testLeaveBalanceEmployeeOutsideDepartment() {
                Department department = new Department("HR", "Human resources");
                department.setId(2L);
                Employee employee = new Employee();
                employee.setDepartement(department);
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findById(3L)).thenReturn(Optional.of(employee));

                assertThrows(EmployeeNotFoundException.class,
                                () -> employeeService.getLeaveBalance(1L, 3L));
        }

        @Test
        @Description("Calculer l'ancienneté d'un employé")
        void testGetEmployeeSeniority() {
                Employee employee = new Employee();
                employee.setDateDeRejoindre(LocalDate.now().minusYears(3).minusMonths(2));
                when(departmentRepository.existsById(1L)).thenReturn(true);
                when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));

                var seniority = employeeService.getLeaveBalanceAncien(1L, 2L);

                assertEquals(3, seniority.get("years"));
                assertTrue(seniority.containsKey("months"));
                assertTrue(seniority.containsKey("days"));
        }

}
