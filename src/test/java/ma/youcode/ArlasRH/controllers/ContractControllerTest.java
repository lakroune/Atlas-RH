package ma.youcode.ArlasRH.controllers;

import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.exceptions.ContractNotFoundException;
import ma.youcode.ArlasRH.exceptions.GlobalExceptionHandler;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.enums.ContratType;
import ma.youcode.ArlasRH.services.ContractService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractController.class)
@Import(GlobalExceptionHandler.class)
class ContractControllerTest {

    private static final String BASE_URL = "/api/v1/employes/5/contracts";

    @Autowired MockMvc mockMvc;
    @MockitoBean ContractService contractService;
    @MockitoBean ContractMapper contractMapper;

    @Test
    void shouldReturnContractHistory() throws Exception {
        when(contractService.getContractHistoryByEmployeeId(5L))
                .thenReturn(List.of(response(10L), response(11L)));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].type").value("CDI"));
    }

    @Test
    void shouldCreateContract() throws Exception {
        Contract contract = new Contract();
        when(contractMapper.toEntity(any(ContractCreateRequest.class))).thenReturn(contract);
        when(contractService.createContract(5L, contract)).thenReturn(response(10L));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validContractJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.grossSalary").value(12000.0))
                .andExpect(jsonPath("$.employeeId").value(5));

        verify(contractService).createContract(5L, contract);
    }

    @Test
    void shouldRejectInvalidContract() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":null,\"grossSalary\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnContractById() throws Exception {
        when(contractService.getContractById(5L, 10L)).thenReturn(response(10L));

        mockMvc.perform(get(BASE_URL + "/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void shouldReturn404ForUnknownContract() throws Exception {
        when(contractService.getContractById(5L, 99L))
                .thenThrow(new ContractNotFoundException("Contract not found."));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contract not found."));
    }

    @Test
    void shouldTerminateContract() throws Exception {
        ContractResponse terminated = response(10L);
        terminated.setActive(false);
        terminated.setEndDate(LocalDate.of(2026, 11, 15));
        when(contractService.terminerContract(5L, 10L)).thenReturn(terminated);

        mockMvc.perform(patch(BASE_URL + "/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.endDate").value("2026-11-15"));
    }

    private ContractResponse response(Long id) {
        return new ContractResponse(id, ContratType.CDI, LocalDate.of(2026, 1, 1), null,
                12000.0, true, 5L);
    }

    private String validContractJson() {
        return """
                {
                  "type": "CDI",
                  "startDate": "2026-01-01",
                  "grossSalary": 12000.0
                }
                """;
    }
}
