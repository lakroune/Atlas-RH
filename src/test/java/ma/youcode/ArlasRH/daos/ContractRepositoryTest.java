package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.enums.ContratType;
import ma.youcode.ArlasRH.models.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContractRepositoryTest {

    @Autowired ContractRepository contractRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentRepository departmentRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        Department department = departmentRepository.save(new Department("IT", "Technology"));
        employee = new Employee();
        employee.setMatricule("EMP-001");
        employee.setFirstName("Sara");
        employee.setLastName("Amrani");
        employee.setEmail("sara@example.com");
        employee.setPosition("Developer");
        employee.setDateDeRejoindre(LocalDate.of(2025, 1, 1));
        employee.setRole(Role.EMPLOYE);
        employee.setDepartement(department);
        employee = employeeRepository.saveAndFlush(employee);
    }

    @Test
    void shouldFindContractHistoryByEmployee() {
        Contract active = contract(ContratType.CDI, LocalDate.of(2025, 1, 1));
        Contract old = contract(ContratType.CDD, LocalDate.of(2024, 1, 1));
        old.setEndDate(LocalDate.of(2024, 12, 31));
        old.setActive(false);
        contractRepository.saveAllAndFlush(java.util.List.of(active, old));

        assertThat(contractRepository.findByEmployee_Id(employee.getId()))
                .hasSize(2)
                .extracting(Contract::getType)
                .containsExactlyInAnyOrder(ContratType.CDI, ContratType.CDD);
    }

    @Test
    void shouldDetectActiveContractForEmployee() {
        contractRepository.saveAndFlush(contract(ContratType.CDI, LocalDate.of(2025, 1, 1)));

        assertThat(contractRepository.existsByEmployee_IdAndIsActiveTrue(employee.getId())).isTrue();
        assertThat(contractRepository.existsByEmployee_IdAndIsActiveTrue(999L)).isFalse();
    }

    private Contract contract(ContratType type, LocalDate startDate) {
        return new Contract(type, startDate, null, 10000.0, employee);
    }
}
