package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import lombok.*;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.enums.ContratType;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {

    private Long id;
    private ContratType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double grossSalary;
    private boolean isActive;
    private Long employeeId;

     
}
