package ma.youcode.ArlasRH.mappers;

import ma.youcode.ArlasRH.dtos.DepartmentCreateRequest;
import ma.youcode.ArlasRH.dtos.DepartmentResponse;
import ma.youcode.ArlasRH.models.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }


    public Department toEntity(DepartmentCreateRequest departmentCreateRequest) {
        return new Department(
                departmentCreateRequest.getName(),
                departmentCreateRequest.getDescription()
        );
    }

    public static class ContractMapper {
    }
}