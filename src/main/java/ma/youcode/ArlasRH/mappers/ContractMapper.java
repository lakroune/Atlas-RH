package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.ContractCreateRequest;
import ma.youcode.ArlasRH.dtos.ContractResponse;
import ma.youcode.ArlasRH.models.Contract;
import ma.youcode.ArlasRH.models.Employee;

@Component

public class ContractMapper {
    public ContractResponse toResponse(Contract contract) {

        return new ContractResponse(
                contract.getId(),
                contract.getType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getGrossSalary(),
                contract.getActive(),
                contract.getEmployee().getId());
    }

    public Contract toEntity(ContractCreateRequest request) {
        Contract contract = new Contract();
        contract.setType(request.getType());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setGrossSalary(request.getGrossSalary());
        return contract;
    }

}
