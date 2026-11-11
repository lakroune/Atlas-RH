package ma.youcode.ArlasRH.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ma.youcode.ArlasRH.dtos.EmployeeCreateRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyExistsException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.mappers.EmployeeCreateMapper;
import ma.youcode.ArlasRH.mappers.EmployeeUpdateMapper;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.enums.Role;
import ma.youcode.ArlasRH.services.EmployeeService;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    private static final String BASE_URL = "/api/v1/departments/1/employes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private EmployeeCreateMapper employeeCreateMapper;

    @MockitoBean
    private EmployeeUpdateMapper employeeUpdateMapper;

    @Test
    @Description ("Test that the getAllEmployees endpoint returns a 200 OK status and a paginated JSON response.")
    void getAllEmployeesReturns200AndPaginatedJson() throws Exception {
        EmployeeResponse response = employeeResponse();
        when(employeeService.getEmployeesByDepartmentId(eq(1L), eq(null), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[0].matricule").value("EMP-001"))
                .andExpect(jsonPath("$.content[0].email").value("sara@example.com"))
                .andExpect(jsonPath("$.content[0].role").value("EMPLOYE"))
                .andExpect(jsonPath("$.content[0].departmentId").value(1));
    }

    @Test
    void createEmployeeReturns201AndEmployeeJson() throws Exception {
        Employee employee = new Employee();
        EmployeeResponse response = employeeResponse();
        when(employeeCreateMapper.toEntity(any(EmployeeCreateRequest.class))).thenReturn(employee);
        when(employeeService.createEmployee(1L, employee)).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validEmployeeJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.firstName").value("Sara"))
                .andExpect(jsonPath("$.lastName").value("Amrani"))
                .andExpect(jsonPath("$.dateDeRejoindre").value("2026-01-10"));

        verify(employeeService).createEmployee(1L, employee);
    }

    @Test
    void createEmployeeReturns400ForInvalidRequest() throws Exception {
        String invalidJson = """
                {
                  "matricule": "",
                  "firstName": "S",
                  "lastName": "",
                  "email": "not-an-email",
                  "dateDeRejoindre": "2026-01-10",
                  "role": null
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(employeeCreateMapper, never()).toEntity(any());
        verify(employeeService, never()).createEmployee(any(), any());
    }

    @Test
    void getEmployeeReturns200AndExpectedJson() throws Exception {
        when(employeeService.getEmployeeById(1L, 10L)).thenReturn(employeeResponse());

        mockMvc.perform(get(BASE_URL + "/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("EMP-001"))
                .andExpect(jsonPath("$.position").value("Developer"));
    }

    @Test
    void getEmployeeReturns404WhenEmployeeDoesNotExist() throws Exception {
        when(employeeService.getEmployeeById(1L, 99L))
                .thenThrow(new EmployeeNotFoundException("Employee not found."));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Employee not found."));
    }

    @Test
    void createEmployeeReturns409WhenMatriculeOrEmailAlreadyExists() throws Exception {
        Employee employee = new Employee();
        when(employeeCreateMapper.toEntity(any(EmployeeCreateRequest.class))).thenReturn(employee);
        when(employeeService.createEmployee(1L, employee))
                .thenThrow(new EmployeeAlreadyExistsException("Matricule already exists."));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validEmployeeJson()))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Matricule already exists."));
    }

    private EmployeeResponse employeeResponse() {
        return new EmployeeResponse(
                10L,
                "EMP-001",
                "Sara",
                "Amrani",
                "sara@example.com",
                "Developer",
                LocalDate.of(2026, 1, 10),
                Role.EMPLOYE,
                1L
        );
    }

    private String validEmployeeJson() {
        return """
                {
                  "matricule": "EMP-001",
                  "firstName": "Sara",
                  "lastName": "Amrani",
                  "email": "sara@example.com",
                  "position": "Developer",
                  "dateDeRejoindre": "2026-01-10",
                  "role": "EMPLOYE"
                }
                """;
    }
}
