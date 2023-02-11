package br.com.ditwowin.api.services;

import br.com.ditwowin.api.dto.AccountDTO;
import br.com.ditwowin.api.enums.StatusAccount;
import br.com.ditwowin.api.models.Account;
import br.com.ditwowin.api.models.Client;
import br.com.ditwowin.api.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public boolean create(Account account) {
        if (Optional.ofNullable(accountRepository.findByClientCpf(account.getClientCpf())).isPresent()) {
            return false;
        }

        accountRepository.save(account);
        return true;
    }

    public boolean changeAccountStatus(String cpf, StatusAccount status) {

        Optional<Account> accountFound = Optional.ofNullable(accountRepository.findByClientCpf(cpf));

        if (accountFound.isEmpty() || !validateStatusEnum(status)){
            return false;
        }

        accountFound.get().setStatusAccount(status);
        accountRepository.saveAndFlush(accountFound.get());
        return true;
    }

    public AccountDTO queryAccount(String  cpf) {

        Optional<Account> accountFound = Optional.ofNullable(accountRepository.findByClientCpf(cpf));

        return accountFound.map(account -> new AccountDTO(
                account.getBalance(),
                account.getNumber(),
                account.getAgency()

        )).orElse(null);

    }

    private boolean validateStatusEnum(StatusAccount status) {
        return switch (status) {
            case ACTIVE, BLOCKED, FROZEN -> true;
            default -> false;
        };
    }

}
