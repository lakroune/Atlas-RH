package ma.youcode.ArlasRH.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ma.youcode.ArlasRH.daos.DepartmentRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.daos.LeaveRequestRepository;
import ma.youcode.ArlasRH.daos.LeaveTypeRepository;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.LeaveRequest;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:arlasrh-integration-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.docker.compose.enabled=false"
})
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    private Department engineering;

    @BeforeEach
    void setUp() {
        leaveRequestRepository.deleteAllInBatch();
        employeeRepository.deleteAllInBatch();
        leaveTypeRepository.deleteAllInBatch();
        departmentRepository.deleteAllInBatch();
        engineering = departmentRepository.saveAndFlush(
                new Department("Engineering", "Product engineering")
        );
    }

    @Test
    void createsEmployeeThroughControllerServiceAndRepository() throws Exception {
        String requestBody = """
                {
                  "matricule": "EMP-100",
                  "firstName": "Sara",
                  "lastName": "Amrani",
                  "email": "sara.amrani@example.com",
                  "position": "Developer",
                  "dateDeRejoindre": "2026-01-10",
                  "role": "EMPLOYE"
                }
                """;

        mockMvc.perform(post("/api/v1/departments/{idD}/employes", engineering.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricule").value("EMP-100"))
                .andExpect(jsonPath("$.departmentId").value(engineering.getId()));

        Employee persistedEmployee = employeeRepository.findAll().get(0);
        assertThat(persistedEmployee.getEmail()).isEqualTo("sara.amrani@example.com");
        assertThat(persistedEmployee.getDepartement().getId()).isEqualTo(engineering.getId());
    }

    @Test
    void searchesEmployeesWithFilterPaginationAndSortingThroughAllLayers() throws Exception {
        employeeRepository.save(employee("EMP-101", "Sarah", "Bennani", "sarah@example.com"));
        employeeRepository.save(employee("EMP-102", "Sara", "Amrani", "sara@example.com"));
        employeeRepository.save(employee("EMP-103", "Youssef", "Alami", "youssef@example.com"));
        employeeRepository.flush();

        mockMvc.perform(get("/api/v1/departments/{idD}/employes", engineering.getId())
                        .param("nom", "sar")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sortBy", "lastName")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].matricule").value("EMP-102"))
                .andExpect(jsonPath("$.content[0].lastName").value("Amrani"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void createsEmployeeAndLeaveRequestThenManagerApprovesIt() throws Exception {
        Employee manager = employeeRepository.saveAndFlush(
                employee("MGR-001", "Nadia", "Alami", "nadia.manager@example.com", Role.MANAGER)
        );
        LeaveType annualLeave = leaveTypeRepository.saveAndFlush(new LeaveType("Congé annuel", 22));

        mockMvc.perform(post("/api/v1/departments/{idD}/employes", engineering.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "matricule": "EMP-200",
                                  "firstName": "Yassine",
                                  "lastName": "Bennani",
                                  "email": "yassine@example.com",
                                  "position": "Developer",
                                  "dateDeRejoindre": "2026-01-10",
                                  "role": "EMPLOYE"
                                }
                                """))
                .andExpect(status().isCreated());

        Employee createdEmployee = employeeRepository.findAllByMatriculeIn(java.util.List.of("EMP-200")).get(0);

        mockMvc.perform(post("/api/v1/managers/{managerId}/employees", manager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"EMP-200\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/Employees/{idE}/demandes", createdEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "startDate": "2026-12-01",
                                  "endDate": "2026-12-03",
                                  "reason": "Congé familial",
                                  "leaveTypeId": %d
                                }
                                """.formatted(annualLeave.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("EN_ATTENTE"));

        LeaveRequest leaveRequest = leaveRequestRepository.findByEmployee_Id(createdEmployee.getId()).get(0);

        mockMvc.perform(patch("/api/v1/managers/{managerId}/demands/{leaveRequestId}/approve",
                        manager.getId(), leaveRequest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"managerComment\":\"Demande approuvée\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROUVEE"))
                .andExpect(jsonPath("$.managerComment").value("Demande approuvée"));

        LeaveRequest approvedRequest = leaveRequestRepository.findById(leaveRequest.getId()).orElseThrow();
        assertThat(approvedRequest.getStatus()).isEqualTo(LeaveStatus.APPROUVEE);
        assertThat(approvedRequest.getDecisionDate()).isEqualTo(LocalDate.now());
        assertThat(approvedRequest.getEmployee().getManager().getId()).isEqualTo(manager.getId());
    }

    @Test
    void refusesToDeleteDepartmentContainingEmployees() throws Exception {
        Employee employee = employeeRepository.saveAndFlush(
                employee("EMP-300", "Salma", "Idrissi", "salma@example.com")
        );

        mockMvc.perform(delete("/api/v1/departments/{id}", engineering.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot delete department because it has employees."));

        assertThat(departmentRepository.existsById(engineering.getId())).isTrue();
        assertThat(employeeRepository.existsById(employee.getId())).isTrue();
    }

    private Employee employee(String matricule, String firstName, String lastName, String email) {
        return employee(matricule, firstName, lastName, email, Role.EMPLOYE);
    }

    private Employee employee(
            String matricule,
            String firstName,
            String lastName,
            String email,
            Role role
    ) {
        return new Employee(
                matricule,
                firstName,
                lastName,
                email,
                "Developer",
                LocalDate.of(2025, 1, 10),
                role,
                engineering,
                null,
                null,
                null,
                null
        );
    }
}
