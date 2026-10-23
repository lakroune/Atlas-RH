package ma.youcode.ArlasRH.services.impls;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ma.youcode.ArlasRH.daos.ContractRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.services.ContractService;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final EmployeeRepository employeeRepository;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
                               EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ContractResponse createContract(Long employeeId, Contract contract) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found."));

        contract.setEmployee(employee);
        Contract savedContract = contractRepository.save(contract);
        return contractMapper.toResponse(savedContract);
    }

    @Override
    public Contract terminateContract(Long contractId, LocalDate endDate) {
        return null;
    }

    @Override
    public List<ContractResponse> getContractHistoryByEmployeeId(Long employeeId) {
        if (!employeeRepository.existsById(employeeId))
            throw new RuntimeException("Employee not found.");
        return contractRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(contractMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return Optional.empty();
    }

    @Override
    public ContractResponse terminerContract(Long idE, Long idC) {
        return null;
    }
}
