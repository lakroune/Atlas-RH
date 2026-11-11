package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.LeaveRequesCreatetRequest;
import ma.youcode.ArlasRH.dtos.LeaveRequestRespense;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.exceptions.LeaveRequestCannotBeCancelledException;
import ma.youcode.ArlasRH.models.enums.LeaveStatus;
import ma.youcode.ArlasRH.services.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveRequestController.class)
@Import(GlobalExceptionHandler.class)
class LeaveRequestControllerTest {

    private static final String BASE_URL = "/Employees/5/demandes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveRequestService leaveRequestService;

    @Test
    void shouldCreateLeaveRequest() throws Exception {
        when(leaveRequestService.createLeaveRequest(eq(5L), any(LeaveRequesCreatetRequest.class)))
                .thenReturn(response(10L, LeaveStatus.EN_ATTENTE));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.reason").value("Annual holiday"))
                .andExpect(jsonPath("$.status").value("EN_ATTENTE"))
                .andExpect(jsonPath("$.employeeId").value(5))
                .andExpect(jsonPath("$.leaveTypeId").value(2));

        verify(leaveRequestService).createLeaveRequest(eq(5L), any(LeaveRequesCreatetRequest.class));
    }

    @Test
    void shouldReturn404WhenEmployeeDoesNotExist() throws Exception {
        when(leaveRequestService.createLeaveRequest(eq(5L), any(LeaveRequesCreatetRequest.class)))
                .thenThrow(new EmployeeNotFoundException("Employee not found."));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found."));
    }

    @Test
    void shouldReturnAllEmployeeLeaveRequests() throws Exception {
        when(leaveRequestService.getAllLeaveRequest(5L)).thenReturn(List.of(
                response(10L, LeaveStatus.EN_ATTENTE),
                response(11L, LeaveStatus.APPROUVEE)
        ));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[1].status").value("APPROUVEE"));
    }

    @Test
    void shouldCancelLeaveRequest() throws Exception {
        when(leaveRequestService.cancelLeaveRequest(10L, 5L))
                .thenReturn(response(10L, LeaveStatus.ANNULEE));

        mockMvc.perform(patch(BASE_URL + "/10/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("ANNULEE"));

        verify(leaveRequestService).cancelLeaveRequest(10L, 5L);
    }

    @Test
    void shouldReturn409WhenLeaveRequestCannotBeCancelled() throws Exception {
        when(leaveRequestService.cancelLeaveRequest(10L, 5L))
                .thenThrow(new LeaveRequestCannotBeCancelledException("Leave request cannot be cancelled."));

        mockMvc.perform(patch(BASE_URL + "/10/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Leave request cannot be cancelled."));
    }

    private LeaveRequestRespense response(Long id, LeaveStatus status) {
        return new LeaveRequestRespense(
                id,
                LocalDate.of(2027, 1, 10),
                LocalDate.of(2027, 1, 12),
                2,
                "Annual holiday",
                status,
                null,
                null,
                5L,
                2L
        );
    }

    private String validRequestJson() {
        return """
                {
                  "startDate": "2027-01-10",
                  "endDate": "2027-01-12",
                  "reason": "Annual holiday",
                  "leaveTypeId": 2
                }
                """;
    }
}
