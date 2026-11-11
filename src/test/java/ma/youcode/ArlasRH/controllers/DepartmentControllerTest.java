package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.exceptions.DepartmentNotFoundException;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.services.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    private static final String BASE_URL = "/api/v1/departments";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    void shouldReturnAllDepartments() throws Exception {
        when(departmentService.getAllDepartements()).thenReturn(List.of(
                new DepartmentResponse(1L, "IT", "Technology"),
                new DepartmentResponse(2L, "HR", "Human resources")
        ));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[1].name").value("HR"));
    }

    @Test
    void shouldReturnDepartmentById() throws Exception {
        when(departmentService.getDepartementById(1L))
                .thenReturn(new DepartmentResponse(1L, "IT", "Technology"));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT"))
                .andExpect(jsonPath("$.description").value("Technology"));
    }

    @Test
    void shouldReturn404WhenDepartmentDoesNotExist() throws Exception {
        when(departmentService.getDepartementById(99L))
                .thenThrow(new DepartmentNotFoundException("Department not found."));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Department not found."));
    }

    @Test
    void shouldCreateDepartment() throws Exception {
        when(departmentService.createDepartement(any(Department.class)))
                .thenReturn(new DepartmentResponse(1L, "Finance", "Finance department"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson("Finance", "Finance department")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Finance"));

        verify(departmentService).createDepartement(argThat(department ->
                "Finance".equals(department.getName())
                        && "Finance department".equals(department.getDescription())));
    }

    @Test
    void shouldUpdateDepartment() throws Exception {
        when(departmentService.updateDepartement(org.mockito.ArgumentMatchers.eq(1L), any(Department.class)))
                .thenReturn(new DepartmentResponse(1L, "Engineering", "Product engineering"));

        mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson("Engineering", "Product engineering")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Engineering"));

        verify(departmentService).updateDepartement(org.mockito.ArgumentMatchers.eq(1L),
                argThat(department -> "Engineering".equals(department.getName())));
    }

    @Test
    void shouldDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartement(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(departmentService).deleteDepartement(1L);
    }

    @Test
    void shouldReturn404WhenDeletionFails() throws Exception {
        doThrow(new DepartmentNotFoundException("Department not found."))
                .when(departmentService).deleteDepartement(99L);

        mockMvc.perform(delete(BASE_URL + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Department not found."));
    }

    private String departmentJson(String name, String description) {
        return """
                {
                  "name": "%s",
                  "description": "%s"
                }
                """.formatted(name, description);
    }
}
