package br.com.ditwowin.api.controllers;

import br.com.ditwowin.api.dto.AccountDTO;
import br.com.ditwowin.api.enums.StatusAccount;
import br.com.ditwowin.api.models.Account;
import br.com.ditwowin.api.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static br.com.ditwowin.api.shared.Utils.*;

@RestController
@RequestMapping(value = "api/v1/account")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("create")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> createNewAccount (@RequestBody @Valid Account account) {
        if (accountService.create(account)) {
            return new ResponseEntity<>(SUCCESS_ACCOUNT_CREATED,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(ERROR_ACCOUNT_EXISTS_ACCOUNT,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("get/{cpf}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> getAccountByCpf (@PathVariable String cpf) {

        Optional<AccountDTO> account = Optional.ofNullable(accountService.queryAccount(cpf));
        if (account.isPresent()) {
            return new ResponseEntity<>(account,HttpStatus.OK);
        }

        return new ResponseEntity<>(ERROR_ACCOUNT_EXISTS_ACCOUNT,HttpStatus.BAD_REQUEST);
    }

    @PutMapping("updateStatus/{cpf}/{newStatus}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> changeAccountStatus (@PathVariable @Valid String cpf, @PathVariable @Valid StatusAccount newStatus) {
        if (accountService.changeAccountStatus(cpf, newStatus)) {
            return new ResponseEntity<>(SUCCESS_ACCOUNT_STATUS_UPDATED,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(ERROR_ACCOUNT_EXISTS_ACCOUNT,HttpStatus.BAD_REQUEST);
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
