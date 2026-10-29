package ma.youcode.ArlasRH.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LeaveTypeRequest {
    @NotBlank(message = "")
    private  String name;
    @NotBlank(message = "")
    private Integer daysPerYear;
}
