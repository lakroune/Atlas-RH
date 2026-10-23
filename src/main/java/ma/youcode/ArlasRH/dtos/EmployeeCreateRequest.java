package ma.youcode.ArlasRH.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import ma.youcode.ArlasRH.models.enums.Role;

import java.time.LocalDate;

public class EmployeeRequest {

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


    public EmployeeRequest() {
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDateDeRejoindre() {
        return dateDeRejoindre;
    }

    public void setDateDeRejoindre(LocalDate dateDeRejoindre) {
        this.dateDeRejoindre = dateDeRejoindre;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}