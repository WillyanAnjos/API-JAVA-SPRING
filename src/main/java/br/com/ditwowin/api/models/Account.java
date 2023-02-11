package br.com.ditwowin.api.models;

import br.com.ditwowin.api.enums.StatusAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @NotNull(message = "Campo numero nao pode estar vazio")
    private int number;

    @NotNull(message = "Campo agencia nao pode estar vazio")
    private int agency;

    @Enumerated(EnumType.ORDINAL)
    private StatusAccount statusAccount;

    @NotBlank(message = "Campo cpf nao pode estar vazio")
    @CPF
    private String clientCpf;

    public boolean checkIfTheBalanceWillBeNegative (BigDecimal amountToDeposit) {
        return balance.subtract(amountToDeposit).compareTo(BigDecimal.ZERO) < 0;
    }
}
