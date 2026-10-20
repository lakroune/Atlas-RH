package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
