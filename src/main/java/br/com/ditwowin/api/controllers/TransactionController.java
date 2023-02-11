package br.com.ditwowin.api.controllers;

import br.com.ditwowin.api.models.Transaction;
import br.com.ditwowin.api.services.TransactionService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.ditwowin.api.shared.Utils.*;

@RestController
@RequestMapping(value = "api/v1/transaction")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("createNewWithDrawMoney/{cpf}/{amountToWithdraw}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> createNewWithDrawMoney (@PathVariable @Valid @CPF String cpf,
                                                     @PathVariable @Valid BigDecimal amountToWithdraw) {
        if (transactionService.withDrawMoney(cpf, amountToWithdraw)) {
            return new ResponseEntity<>(SUCCESS_TRANSACTION_WITH_DRAWMONEY,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(ERROR_TRANSACTION_WITH_DRAWMONEY,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("createNewDeposit/{cpf}/{amoutToDeposit}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> createNewDeposit (@PathVariable @Valid @CPF String cpf,
                                                     @PathVariable @Valid BigDecimal amoutToDeposit) {
        if (transactionService.deposit(cpf, amoutToDeposit)) {
            return new ResponseEntity<>(SUCCESS_TRANSACTION_WITH_DEPOSIT,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(ERROR_TRANSACTION_WITH_DEPOSIT,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("filterTransactionExtract/{cpf}/{start}/{end}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> filterTransactionExtract (@PathVariable @Valid @CPF String cpf,
                                                       @PathVariable  String start,
                                                       @PathVariable  String end) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        Optional<List<Transaction>> transactions = Optional.ofNullable(transactionService.
                filterTransactionExtract(cpf, LocalDateTime.parse(start,formatter), LocalDateTime.parse(end,formatter)));

        if (transactions.isPresent()) {
            return new ResponseEntity<>(transactions,HttpStatus.OK);
        }

        return new ResponseEntity<>(ERROR_TRANSACTION_WITH_FILTER,HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
