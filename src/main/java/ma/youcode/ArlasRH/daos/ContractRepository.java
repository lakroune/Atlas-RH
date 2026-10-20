package ma.youcode.ArlasRH.daos;

import ma.youcode.ArlasRH.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDAO extends JpaRepository<Contract, Long> {
}
