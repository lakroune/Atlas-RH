package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import ma.youcode.ArlasRH.models.enums.ContratType;

public class ContractResponse {

    private Long id;
    private ContratType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double grossSalary;
    private Long employeeId;

    public ContractResponse() {
    }

    public ContractResponse(Long id, ContratType type, LocalDate startDate, LocalDate endDate, Double grossSalary, Long employeeId) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grossSalary = grossSalary;
        this.employeeId = employeeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
