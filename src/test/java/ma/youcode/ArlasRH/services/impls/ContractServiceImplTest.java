package ma.youcode.ArlasRH.services.impls;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
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
import ma.youcode.ArlasRH.exceptions.ContractAlreadyTerminatedException;
import ma.youcode.ArlasRH.exceptions.ContractNotFoundException;
import ma.youcode.ArlasRH.exceptions.EmployeeAlreadyHasActiveContractException;
import ma.youcode.ArlasRH.exceptions.EmployeeNotFoundException;
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

    @Test
    @Description("Refuser un deuxième contrat actif pour le même employé")
    void testContratActifDejaExistant() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        Contract contract = new Contract();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));
        when(contractRepository.existsByEmployee_IdAndIsActiveTrue(employeeId))
                .thenReturn(true);

        assertThrows(
                EmployeeAlreadyHasActiveContractException.class,
                () -> contractService.createContract(employeeId, contract)
        );

        verify(contractRepository, never()).save(contract);
    }

    @Test
    @Description("Refuser la création d'un contrat pour un employé inexistant")
    void testCreateContractEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> contractService.createContract(99L, new Contract())
        );
        verify(contractRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @Description("Retourner l'historique des contrats d'un employé")
    void testGetContractHistory() {
        Contract firstContract = new Contract();
        Contract secondContract = new Contract();
        ContractResponse firstResponse = new ContractResponse();
        ContractResponse secondResponse = new ContractResponse();
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findByEmployee_Id(1L)).thenReturn(List.of(firstContract, secondContract));
        when(contractMapper.toResponse(firstContract)).thenReturn(firstResponse);
        when(contractMapper.toResponse(secondContract)).thenReturn(secondResponse);

        List<ContractResponse> history = contractService.getContractHistoryByEmployeeId(1L);

        assertEquals(List.of(firstResponse, secondResponse), history);
    }

    @Test
    @Description("Refuser l'historique pour un employé inexistant")
    void testGetContractHistoryEmployeeNotFound() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> contractService.getContractHistoryByEmployeeId(99L)
        );
        verify(contractRepository, never()).findByEmployee_Id(99L);
    }

    @Test
    @Description("Terminer le contrat actif d'un employé")
    void testTerminateEmployeeContract() {
        Contract contract = new Contract();
        ContractResponse expectedResponse = new ContractResponse();
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(10L)).thenReturn(Optional.of(contract));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponse(contract)).thenReturn(expectedResponse);

        ContractResponse response = contractService.terminerContract(1L, 10L);

        assertSame(expectedResponse, response);
        assertEquals(LocalDate.now(), contract.getEndDate());
        assertFalse(contract.getActive());
        verify(contractRepository).save(contract);
    }

    @Test
    @Description("Refuser la terminaison pour un employé inexistant")
    void testTerminateContractEmployeeNotFound() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> contractService.terminerContract(99L, 10L)
        );
        verify(contractRepository, never()).findById(10L);
    }

    @Test
    @Description("Refuser la terminaison d'un contrat inexistant")
    void testTerminateContractNotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ContractNotFoundException.class,
                () -> contractService.terminerContract(1L, 99L)
        );
    }

    @Test
    @Description("Refuser de terminer une seconde fois un contrat")
    void testTerminateAlreadyTerminatedContract() {
        Contract contract = new Contract();
        contract.setEndDate(LocalDate.of(2026, 1, 1));
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(10L)).thenReturn(Optional.of(contract));

        assertThrows(
                ContractAlreadyTerminatedException.class,
                () -> contractService.terminerContract(1L, 10L)
        );
        verify(contractRepository, never()).save(contract);
    }

    @Test
    @Description("Récupérer un contrat par son identifiant")
    void testGetContractById() {
        Contract contract = new Contract();
        ContractResponse expectedResponse = new ContractResponse();
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(10L)).thenReturn(Optional.of(contract));
        when(contractMapper.toResponse(contract)).thenReturn(expectedResponse);

        assertSame(expectedResponse, contractService.getContractById(1L, 10L));
    }

    @Test
    @Description("Refuser la consultation pour un employé inexistant")
    void testGetContractByIdEmployeeNotFound() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> contractService.getContractById(99L, 10L)
        );
    }

    @Test
    @Description("Refuser la consultation d'un contrat inexistant")
    void testGetContractByIdNotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ContractNotFoundException.class,
                () -> contractService.getContractById(1L, 99L)
        );
    }

    @Test
    @Description("Documenter la méthode de terminaison non implémentée")
    void testTerminateContractMethodCurrentlyReturnsNull() {
        assertNull(contractService.terminateContract(10L, LocalDate.of(2026, 12, 31)));
    }



   

    
}
