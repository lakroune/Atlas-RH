package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import ma.youcode.ArlasRH.models.enums.Role;

public class EmployeeUpdateRequest {

    private String position;

    @NotNull(message = "Role is vide")
    private Role role;

    public EmployeeUpdateRequest() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
