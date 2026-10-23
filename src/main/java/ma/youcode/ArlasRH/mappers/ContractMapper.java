package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;

@Component
public class ContractMapper {

    public Contract toEntity(ContractCreateRequest request, Employee employee) {
        Contract contract = new Contract();
        contract.setType(request.getType());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setGrossSalary(request.getGrossSalary());
        contract.setEmployee(employee);
        return contract;
    }

    public ContractResponse toResponse(Contract contract) {
        if (contract == null) {
            return null;
        }

        Long employeeId = contract.getEmployee().getId();

        return new ContractResponse(
                contract.getId(),
                contract.getType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getGrossSalary(),
                employeeId);
    }
}
