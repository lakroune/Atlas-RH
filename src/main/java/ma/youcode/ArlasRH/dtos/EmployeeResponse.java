package ma.youcode.ArlasRH.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.ArlasRH.models.enums.Role;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String matricule;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private LocalDate dateDeRejoindre;
    private Role role;
    private Long departmentId;
}