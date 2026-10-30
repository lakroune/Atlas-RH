package ma.youcode.ArlasRH.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequesCreatetRequest {

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private LocalDate endDate;

    @NotNull(message = "Le nombre de jours travaillés est obligatoire")
    private Integer workingDay;

    @NotBlank(message = "La raison est obligatoire")
    private String reason;

    @NotNull(message = "Le type de congé est obligatoire")
    private Long leaveTypeId;

}
