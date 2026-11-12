package ma.youcode.ArlasRH.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.enums.Role;

@SpringBootTest(properties = {
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

    private Department engineering;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
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

    private Employee employee(String matricule, String firstName, String lastName, String email) {
        return new Employee(
                matricule,
                firstName,
                lastName,
                email,
                "Developer",
                LocalDate.of(2025, 1, 10),
                Role.EMPLOYE,
                engineering,
                null,
                null,
                null,
                null
        );
    }
}
