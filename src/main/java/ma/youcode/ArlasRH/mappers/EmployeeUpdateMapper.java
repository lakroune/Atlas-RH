package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.dtos.EmployeeUpdateRequest;
import ma.youcode.ArlasRH.models.Employee;

@Component
public class EmployeeUpdateMapper {

    public EmployeeResponse toResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getMatricule(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPosition(),
                employee.getDateDeRejoindre(),
                employee.getRole(),
                employee.getDepartement().getId());
    }

    public Employee toEntity(EmployeeUpdateRequest request) {

        Employee updatedEmployee = new Employee();
        updatedEmployee.setPosition(request.getPosition());
        updatedEmployee.setRole(request.getRole());

        return updatedEmployee;
    }

}
