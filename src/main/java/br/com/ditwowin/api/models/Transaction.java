package br.com.ditwowin.api.models;

import br.com.ditwowin.api.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sender_account_id")
    @NotNull(message = "O remetente nao pode estar vazio")
    private  Account senderAccount;

    @NotNull(message = "O valor nao pode estar vazio")
    private BigDecimal amountSent;

    @CreatedDate
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;

}
