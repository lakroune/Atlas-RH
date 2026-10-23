package ma.youcode.ArlasRH.services.impls;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.youcode.ArlasRH.daos.ContractRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.enums.ContratType;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    private ContractServiceImpl contractService;

    @BeforeEach
    void setUp() {
        contractService = new ContractServiceImpl(contractRepository, new ContractMapper(), employeeRepository);
    }

    @Test
    void createContract_shouldSaveContractAndReturnResponse() {
        Employee employee = new Employee();
        employee.setId(12L);

        Contract contract = new Contract();
        contract.setType(ContratType.CDI);
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2025, 1, 1));
        contract.setGrossSalary(40000.0);

        when(employeeRepository.findById(12L)).thenReturn(Optional.of(employee));
        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract saved = invocation.getArgument(0);
            saved.setId(99L);
            return saved;
        });

        ContractResponse response = contractService.createContract(12L, contract);

        assertNotNull(response);
        assertEquals(99L, response.getId());
        assertEquals(ContratType.CDI, response.getType());
        assertEquals(12L, response.getEmployeeId());
        assertEquals(contract.getEmployee(), employee);
    }
}
