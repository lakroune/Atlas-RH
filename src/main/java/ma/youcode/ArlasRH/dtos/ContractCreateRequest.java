package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.youcode.ArlasRH.models.enums.ContratType;

public class ContractCreateRequest {

    @NotNull(message = "Contract type is required")
    private ContratType type;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Gross salary is required")
    @Positive(message = "Gross salary must be positive")
    private Double grossSalary;

    public ContractCreateRequest() {
    }

    public ContratType getType() {
        return type;
    }

    public void setType(ContratType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }
}
