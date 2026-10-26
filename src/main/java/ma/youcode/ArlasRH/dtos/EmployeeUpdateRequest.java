package ma.youcode.ArlasRH.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.ArlasRH.models.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest {

    private String position;

    @NotNull(message = "Role is vide")
    private Role role;

     
}
