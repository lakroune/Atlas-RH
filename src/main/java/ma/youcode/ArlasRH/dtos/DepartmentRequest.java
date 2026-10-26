package ma.youcode.ArlasRH.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Department name is vide")
    @Size(min = 3, max = 100,
            message = "Department name br must 3 -100 characters")
    private String name;

    @NotBlank(message = "Department description is vide")
    @Size(min = 5, max = 255, message = "Department description be must 5 and 255 characters")
    private String description;

   

    
}