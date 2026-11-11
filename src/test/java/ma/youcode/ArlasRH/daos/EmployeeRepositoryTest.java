package ma.youcode.ArlasRH.daos;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ma.youcode.ArlasRH.models.Department;
import ma.youcode.ArlasRH.models.Employee;
import ma.youcode.ArlasRH.models.enums.Role;

@DataJpaTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department engineering;
    private Department humanResources;

    @BeforeEach
    void setUp() {
        engineering = departmentRepository.save(new Department("Engineering", "Product engineering"));
        humanResources = departmentRepository.save(new Department("Human Resources", "People operations"));

        employeeRepository.save(employee("EMP-001", "Sara", "Amrani", "sara@example.com", engineering));
        employeeRepository.save(employee("EMP-002", "sarah", "Bennani", "sarah@example.com", engineering));
        employeeRepository.save(employee("EMP-003", "Youssef", "El Amrani", "youssef@example.com", engineering));
        employeeRepository.save(employee("EMP-004", "Nadia", "Zerouali", "nadia@example.com", humanResources));
        employeeRepository.flush();
    }

    @Test
    void searchesEmployeesByFirstNameIgnoringCase() {
        Page<Employee> result = employeeRepository
                .findByDepartement_IdAndFirstNameContainingIgnoreCase(
                        engineering.getId(),
                        "SAR",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .extracting(Employee::getMatricule)
                .containsExactlyInAnyOrder("EMP-001", "EMP-002");
    }

    @Test
    void filtersEmployeesByDepartmentAndLastName() {
        Page<Employee> result = employeeRepository
                .findByDepartement_IdAndLastNameContainingIgnoreCase(
                        engineering.getId(),
                        "amrani",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .extracting(Employee::getMatricule)
                .containsExactlyInAnyOrder("EMP-001", "EMP-003");
        assertThat(result.getContent())
                .allMatch(employee -> employee.getDepartement().getId().equals(engineering.getId()));
    }

    @Test
    void paginatesEmployeesWithinDepartment() {
        Page<Employee> firstPage = employeeRepository.findByDepartement_Id(
                engineering.getId(),
                PageRequest.of(0, 2, Sort.by("matricule").ascending())
        );
        Page<Employee> secondPage = employeeRepository.findByDepartement_Id(
                engineering.getId(),
                PageRequest.of(1, 2, Sort.by("matricule").ascending())
        );

        assertThat(firstPage.getTotalElements()).isEqualTo(3);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.getContent())
                .extracting(Employee::getMatricule)
                .containsExactly("EMP-001", "EMP-002");
        assertThat(secondPage.getContent())
                .extracting(Employee::getMatricule)
                .containsExactly("EMP-003");
    }

    @Test
    void sortsEmployeesByLastNameDescending() {
        Page<Employee> result = employeeRepository.findByDepartement_Id(
                engineering.getId(),
                PageRequest.of(0, 10, Sort.by("lastName").descending())
        );

        assertThat(result.getContent())
                .extracting(Employee::getLastName)
                .containsExactly("El Amrani", "Bennani", "Amrani");
    }

    private Employee employee(
            String matricule,
            String firstName,
            String lastName,
            String email,
            Department department
    ) {
        return new Employee(
                matricule,
                firstName,
                lastName,
                email,
                "Developer",
                LocalDate.of(2025, 1, 10),
                Role.EMPLOYE,
                department,
                null,
                null,
                null,
                null
        );
    }
}
