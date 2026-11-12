package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.LeaveType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeaveTypeRepositoryTest {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Test
    void shouldPersistAndReadLeaveType() {
        LeaveType saved = leaveTypeRepository.saveAndFlush(new LeaveType("Annual", 22));

        assertThat(leaveTypeRepository.findById(saved.getId()))
                .isPresent()
                .get()
                .satisfies(type -> {
                    assertThat(type.getName()).isEqualTo("Annual");
                    assertThat(type.getDaysPerYear()).isEqualTo(22);
                });
    }

    @Test
    void shouldDeleteLeaveType() {
        LeaveType saved = leaveTypeRepository.saveAndFlush(new LeaveType("Sick", 10));

        leaveTypeRepository.deleteById(saved.getId());
        leaveTypeRepository.flush();

        assertThat(leaveTypeRepository.existsById(saved.getId())).isFalse();
    }
}
