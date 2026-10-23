package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.EmployeeCreateRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.models.Employee;

@Component
public class EmployeeMapper {

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

    public Employee toEntity(EmployeeCreateRequest employeeCreateRequest) {

        Employee employee = new Employee();

        employee.setMatricule(employeeCreateRequest.getMatricule());
        employee.setFirstName(employeeCreateRequest.getFirstName());
        employee.setLastName(employeeCreateRequest.getLastName());
        employee.setEmail(employeeCreateRequest.getEmail());
        employee.setPosition(employeeCreateRequest.getPosition());
        employee.setDateDeRejoindre(employeeCreateRequest.getDateDeRejoindre());
        employee.setRole(employeeCreateRequest.getRole());
        return employee;
    }
}