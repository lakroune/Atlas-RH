package ma.youcode.ArlasRH.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.services.ContractService;

@RestController
@RequestMapping("/api/v1/employes/{idE}/contracts")
@Tag(name = "Contracts", description = "Contract management APIs")
public class ContractController {

    private final ContractService contractService;
    private final ContractMapper contractMapper;

    public ContractController(ContractService contractService, ContractMapper contractMapper) {
        this.contractService = contractService;
        this.contractMapper = contractMapper;
    }

    @Operation(
            summary = "Get all contracts for an employee",
            description = "Returns the complete contract history for the given employee."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contracts retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ContractResponse.class))
                    )
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContractResponse> getAllContractsByEmployee(@PathVariable Long idE) {
        return contractService.getContractHistoryByEmployeeId(idE)
                .stream()
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Create a contract",
            description = "Creates a new contract for the specified employee."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Contract created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContractResponse.class)
                    )
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContractResponse createContract(@PathVariable Long idE, @Valid @RequestBody ContractCreateRequest request) {
        Contract contract = contractMapper.toEntity(request);
        return contractService.createContract(idE, contract);
    }

    @Operation(
            summary = "Get a contract by ID",
            description = "Returns the contract matching the given ID for the selected employee."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContractResponse.class)
                    )
            )
    })
    @GetMapping("/{idC}")
    @ResponseStatus(HttpStatus.OK)
    public ContractResponse getContractById(@PathVariable Long idE, @PathVariable Long idC) {
        return contractService.getContractById(idE, idC);
    }

    @Operation(
            summary = "Terminate a contract",
            description = "Ends the selected employee contract by setting its end date."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract terminated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContractResponse.class)
                    )
            )
    })
    @PatchMapping("/{idC}")
    @ResponseStatus(HttpStatus.OK)
    public ContractResponse terminerContract(@PathVariable Long idE, @PathVariable Long idC) {
        return contractService.terminerContract(idE, idC);
    }

}
