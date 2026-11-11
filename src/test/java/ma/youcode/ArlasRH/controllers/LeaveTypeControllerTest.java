package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.LeaveTypeRequest;
import ma.youcode.ArlasRH.dtos.LeaveTypeResponse;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.exceptions.LeaveTypeNotFoundException;
import ma.youcode.ArlasRH.models.LeaveType;
import ma.youcode.ArlasRH.services.LeaveTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveTypeController.class)
@Import(GlobalExceptionHandler.class)
class LeaveTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveTypeService leaveTypeService;

    @Test
    void shouldReturnAllLeaveTypes() throws Exception {
        when(leaveTypeService.getAllLeaveTypes()).thenReturn(List.of(
                new LeaveTypeResponse("Annual", 22),
                new LeaveTypeResponse("Sick", 10)
        ));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Annual"))
                .andExpect(jsonPath("$[0].daysPerYear").value(22))
                .andExpect(jsonPath("$[1].name").value("Sick"));
    }

    @Test
    void shouldCreateLeaveType() {
        LeaveTypeController controller = new LeaveTypeController(leaveTypeService);
        LeaveTypeResponse expected = new LeaveTypeResponse("Annual", 22);
        when(leaveTypeService.createLeaveType(argThat(type ->
                "Annual".equals(type.getName()) && Integer.valueOf(22).equals(type.getDaysPerYear()))))
                .thenReturn(expected);

        LeaveTypeResponse actual = controller.createLeaveType(new LeaveTypeRequest("Annual", 22));

        assertThat(actual).isSameAs(expected);
        verify(leaveTypeService).createLeaveType(argThat(type ->
                "Annual".equals(type.getName()) && Integer.valueOf(22).equals(type.getDaysPerYear())));
    }

    @Test
    void shouldReturnLeaveTypeById() throws Exception {
        when(leaveTypeService.getLeaveTypeById(1L))
                .thenReturn(new LeaveTypeResponse("Annual", 22));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Annual"))
                .andExpect(jsonPath("$.daysPerYear").value(22));
    }

    @Test
    void shouldReturn404WhenLeaveTypeDoesNotExist() throws Exception {
        when(leaveTypeService.getLeaveTypeById(99L))
                .thenThrow(new LeaveTypeNotFoundException("Leave type not found with id: 99"));

        mockMvc.perform(get("/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Leave type not found with id: 99"));
    }

    @Test
    void shouldDeleteLeaveType() throws Exception {
        doNothing().when(leaveTypeService).deleteLeaveType(1L);

        mockMvc.perform(delete("/1"))
                .andExpect(status().isNoContent());

        verify(leaveTypeService).deleteLeaveType(1L);
    }

    @Test
    void shouldReturn404WhenDeletingUnknownLeaveType() throws Exception {
        doThrow(new LeaveTypeNotFoundException("Leave type not found with id: 99"))
                .when(leaveTypeService).deleteLeaveType(99L);

        mockMvc.perform(delete("/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Leave type not found with id: 99"));
    }
}
