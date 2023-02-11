package br.com.ditwowin.api.services;

import br.com.ditwowin.api.enums.StatusAccount;
import br.com.ditwowin.api.enums.TransactionType;
import br.com.ditwowin.api.models.Account;
import br.com.ditwowin.api.models.Client;
import br.com.ditwowin.api.models.Transaction;
import br.com.ditwowin.api.repositories.AccountRepository;
import br.com.ditwowin.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final BigDecimal maximumDailyWithdrawalAmountAllowed = new BigDecimal("2.000");

    public List<Transaction> filterTransactionExtract(String cpf, LocalDateTime start, LocalDateTime end) {
        Optional<Account> accountFounded = Optional.ofNullable(accountRepository.findByClientCpf(cpf));

        if (!validateByFilter(accountFounded, start, end)) {
            return new ArrayList<>();
        }

        return transactionRepository.filterByTransactionDate(accountFounded.get(), start,end);
    }

    private boolean validateByFilter(Optional<Account> account, LocalDateTime start, LocalDateTime end) {

        if (start == null) {
            return false;
        }

        if (end == null) {
            return false;
        }

        if (transactionRepository.filterByTransactionDate(account.get(),start,end).isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean withDrawMoney(String accountCpf, BigDecimal amountToWithdraw) {

        Optional<Account> accountFounded = Optional.ofNullable(accountRepository.findByClientCpf(accountCpf));

        if (!validateToWithdraw(accountFounded, amountToWithdraw)) return false;

        accountFounded.get().setBalance(accountFounded.get().subtractBalance(amountToWithdraw));
        accountRepository.saveAndFlush(accountFounded.get());

        Transaction transaction = new Transaction();
        transaction.setAmountSent(amountToWithdraw);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSenderAccount(accountFounded.get());

        transactionRepository.save(transaction);
        return true;
    }

    private boolean validateToWithdraw(Optional<Account> account, BigDecimal amountToWithdraw) {
        if (account.isEmpty()) {
            return false;
        }

        if (account.get().getStatusAccount() != StatusAccount.ACTIVE) {
            return false;
        }

        if (amountToWithdraw == null || amountToWithdraw.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (account.get().checkIfTheBalanceWillBeNegative(amountToWithdraw)) {
            return false;
        }

        if (amountToWithdraw.compareTo(maximumDailyWithdrawalAmountAllowed) < 0) {
            return false;
        }

        return true;
    }


    //Depositar
    public boolean deposit(String accountCpf, BigDecimal amountToDeposit) {

        Optional<Account> accountFounded = Optional.ofNullable(accountRepository.findByClientCpf(accountCpf));

        if (!validateToDeposit(accountFounded)) return false;

        accountFounded.get().setBalance(accountFounded.get().getBalance().add(amountToDeposit));
        accountRepository.saveAndFlush(accountFounded.get());

        Transaction transaction = new Transaction();
        transaction.setAmountSent(amountToDeposit);
        transaction.setTransactionType(TransactionType.DEPOSITED);
        transaction.setSenderAccount(accountFounded.get());

        transactionRepository.save(transaction);
        return true;
    }

    public boolean validateToDeposit(Optional<Account> account) {
        if (account.isEmpty()) {
            return false;
        }

        if (account.get().getStatusAccount() != StatusAccount.ACTIVE) {
            return false;
        }

        return true;
    }


}
