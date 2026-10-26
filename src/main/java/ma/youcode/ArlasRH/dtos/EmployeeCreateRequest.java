package ma.youcode.ArlasRH.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
public class EmployeeCreateRequest {

    @NotBlank(message = "Matricule is vide")
    @Size(min = 3, max = 20, message = "Matricule must  3 and 20 char")
    private String matricule;

    @NotBlank(message = "First name is vide")
    @Size(min = 2, max = 50, message = "First name must  2 and 50 char")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must  2 and 50 char")
    private String lastName;

    @NotBlank(message = "Email is vide")
    @Email(message = "Email not valid")
    private String email;

    private String position;

    @NotNull(message = "")
    @PastOrPresent(message = "not valide")
    private LocalDate dateDeRejoindre;

    @NotNull(message = "Role is vide")
    private Role role;


   
}