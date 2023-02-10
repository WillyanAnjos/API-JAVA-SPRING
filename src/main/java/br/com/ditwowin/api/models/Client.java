package br.com.ditwowin.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank( message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @NotBlank( message = "Cpf cannot be empty")
    @CPF
    @Size(min = 11, max = 11, message = "CPF must contain 11 digits")
    @Column(name = "cpf")
    private String cpf;

    @NotNull( message = "Date of birth cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;



    public boolean validateName() {
        if (name != null && name.length() > 0) {
            return true;
        }

        return false;
    }
}