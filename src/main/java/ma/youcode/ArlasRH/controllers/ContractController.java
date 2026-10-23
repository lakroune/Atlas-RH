package ma.youcode.ArlasRH.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContractResponse createContract(@PathVariable Long idE,
            @Valid @RequestBody ContractCreateRequest request) {

        Contract contract = contractMapper.toEntity(request);
        return contractService.createContract(idE, contract);
    }
}
