package ma.youcode.ArlasRH.mappers;

import org.springframework.stereotype.Component;

import ma.youcode.ArlasRH.dtos.EmployeeRequest;
import ma.youcode.ArlasRH.dtos.EmployeeResponse;
import ma.youcode.ArlasRH.models.Department;
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

    public Employee toEntity(EmployeeRequest employeeRequest) {

        Employee employee = new Employee();

        employee.setMatricule(employeeRequest.getMatricule());
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPosition(employeeRequest.getPosition());
        employee.setDateDeRejoindre(employeeRequest.getDateDeRejoindre());
        employee.setRole(employeeRequest.getRole());
        return employee;
    }
}