package ma.youcode.ArlasRH.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.services.ContractService;

@RestController
@RequestMapping("/api/v1/employes/{idE}/contracts")
public class ContractController {

    private final ContractService contractService;
    private final ContractMapper contractMapper;

    public ContractController(ContractService contractService, ContractMapper contractMapper) {
        this.contractService = contractService;
        this.contractMapper = contractMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContractResponse> getAllContractsByEmployee(@PathVariable Long idE) {
        return contractService.getContractHistoryByEmployeeId(idE)
                .stream()
                .collect(Collectors.toList());
    }
}
