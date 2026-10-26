package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.ArlasRH.models.enums.ContratType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractCreateRequest {

    @NotNull(message = "Contract type is required")
    private ContratType type;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Gross salary is required")
    @Positive(message = "Gross salary must be positive")
    private Double grossSalary;

}
