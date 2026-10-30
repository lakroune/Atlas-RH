package ma.youcode.ArlasRH.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestAprouveRequest {
    @NotBlank(message = "Manager comment is required")
    private String managerComment;

}
