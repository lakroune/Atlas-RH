package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.LeaveRequestAprouveRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.exceptions.ManagerNotFoundException;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.models.enums.Role;
import ma.youcode.ArlasRH.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.PathMatcher;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
@Import(GlobalExceptionHandler.class)
class ManagerControllerTest {

    private static final String BASE_URL = "/api/v1/managers/7";

    @Autowired MockMvc mockMvc;
    @MockitoBean EmployeeService employeeService;
    @MockitoBean PathMatcher pathMatcher;

    @Test
    void shouldReturnManagerTeam() throws Exception {
        when(employeeService.getEmployeesByManager_Id(7L)).thenReturn(List.of(employeeResponse()));

        mockMvc.perform(get(BASE_URL + "/team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].matricule").value("EMP-001"));
    }

    @Test
    void shouldReturn404ForUnknownManager() throws Exception {
        when(employeeService.getEmployeesByManager_Id(99L))
                .thenThrow(new ManagerNotFoundException("Manager not found."));

        mockMvc.perform(get("/api/v1/managers/99/team"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Manager not found."));
    }

    @Test
    void shouldAssignEmployeesToManager() throws Exception {
        doNothing().when(employeeService).affecterEmployesAuManager(7L, List.of("EMP-001", "EMP-002"));

        mockMvc.perform(post(BASE_URL + "/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"EMP-001\",\"EMP-002\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("manager")));

        verify(employeeService).affecterEmployesAuManager(7L, List.of("EMP-001", "EMP-002"));
    }

    @Test
    void shouldReturnPendingLeaveRequests() throws Exception {
        when(employeeService.getLeaveRequestsByManager(7L)).thenReturn(List.of(leaveResponse()));

        mockMvc.perform(get(BASE_URL + "/demands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("EN_ATTENTE"));
    }

    @Test
    void shouldApproveLeaveRequest() throws Exception {
        when(employeeService.approveLeaveRequest(eq(7L), eq(10L), any(LeaveRequestAprouveRequest.class)))
                .thenReturn(leaveResponse());

        mockMvc.perform(patch(BASE_URL + "/demands/10/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"managerComment\":\"Approved\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void shouldRejectLeaveRequest() throws Exception {
        LeaveRequestRespense rejected = leaveResponse();
        rejected.setStatus(LeaveStatus.REFUSEE);
        when(employeeService.rejectLeaveRequest(7L, 10L)).thenReturn(rejected);

        mockMvc.perform(patch(BASE_URL + "/demands/10/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REFUSEE"));
    }

    private EmployeeResponse employeeResponse() {
        return new EmployeeResponse(5L, "EMP-001", "Sara", "Amrani", "sara@example.com",
                "Developer", LocalDate.of(2025, 1, 1), Role.EMPLOYE, 1L);
    }

    private LeaveRequestRespense leaveResponse() {
        return new LeaveRequestRespense(10L, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 2),
                2, "Holiday", LeaveStatus.EN_ATTENTE, null, null, 5L, 2L);
    }
}
