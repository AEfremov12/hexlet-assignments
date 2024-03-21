package exercise.dto;

// BEGIN

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class GuestCreateDTO {


    @NotEmpty
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?")
    private String phoneNumber;

    @Digits(integer = 4, fraction = 0)
    @Min(value = 1000)
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;
}
// END
