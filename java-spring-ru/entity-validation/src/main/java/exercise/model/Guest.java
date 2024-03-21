package exercise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;




@Entity
@Table(name = "guests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    // BEGIN

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
    
    // END

    @CreatedDate
    private LocalDate createdAt;
}
