package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldDetectExistingDepartmentName() {
        departmentRepository.saveAndFlush(new Department("Finance", "Finance department"));

        assertThat(departmentRepository.existsByName("Finance")).isTrue();
        assertThat(departmentRepository.existsByName("Unknown")).isFalse();
    }

    @Test
    void shouldPersistAndFindDepartmentById() {
        Department saved = departmentRepository.saveAndFlush(new Department("IT", "Technology department"));

        assertThat(departmentRepository.findById(saved.getId()))
                .isPresent()
                .get()
                .extracting(Department::getName, Department::getDescription)
                .containsExactly("IT", "Technology department");
    }
}
