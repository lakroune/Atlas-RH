package ma.youcode.ArlasRH.services;

import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.models.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractService {
    /**
     *
     * @param employeeId
     * @param contract
     * @return
     */
    Contract createContract(Long employeeId, Contract contract);

    /**
     *
     * @param contractId
     * @param endDate
     * @return
     */
    Contract terminateContract(Long contractId, LocalDate endDate);

    /**
     *
     * @param employeeId
     * @return
     */
    List<ContractResponse> getContractHistoryByEmployeeId(Long employeeId);

    /**
     *
     * @param id
     * @return
     */
    Optional<Contract> getContractById(Long id);
}