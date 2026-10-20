package ma.youcode.ArlasRH.services.impls;

import ma.youcode.ArlasRH.daos.ContractRepository;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.services.ContractService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Contract createContract(Long employeeId, Contract contract) {
        return null;
    }

    @Override
    public Contract terminateContract(Long contractId, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Contract> getContractHistoryByEmployeeId(Long employeeId) {
        return List.of();
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return Optional.empty();
    }
}
