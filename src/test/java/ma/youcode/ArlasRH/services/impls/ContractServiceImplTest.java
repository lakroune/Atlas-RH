package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import ma.youcode.ArlasRH.daos.ContractRepository;
import ma.youcode.ArlasRH.daos.EmployeeRepository;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.mappers.ContractMapper;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

   

    @Test
    @Description ("Test de la vérification de l'existence d'un contrat unique en cours pour un employé")
    void testContratUniqueEnCours() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        Contract contract = new Contract();
        ContractResponse expectedResponse = new ContractResponse();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));
        when(contractRepository.save(contract))
                .thenReturn(contract);
        when(contractMapper.toResponse(contract))
                .thenReturn(expectedResponse);

        // Act
        ContractResponse actualResponse =
                contractService.createContract(employeeId, contract);

        // Assert
        assertSame(employee, contract.getEmployee());
        assertSame(expectedResponse, actualResponse);
        verify(contractRepository).save(contract);
        verify(contractMapper).toResponse(contract);
    }



   

    
}
