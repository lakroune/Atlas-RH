package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByEmployee_Id(Long employeeId);
}
