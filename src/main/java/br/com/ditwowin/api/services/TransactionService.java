package br.com.ditwowin.api.services;

import br.com.ditwowin.api.enums.StatusAccount;
import br.com.ditwowin.api.enums.TransactionType;
import br.com.ditwowin.api.models.Account;
import br.com.ditwowin.api.models.Transaction;
import br.com.ditwowin.api.repositories.AccountRepository;
import br.com.ditwowin.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final BigDecimal maximumDailyWithdrawalAmountAllowed = new BigDecimal("2.000");

    public List<Transaction> filterTransactionExtract(Account account, LocalDateTime start, LocalDateTime end) {

        if (start == null || end == null || transactionRepository.filterByTransactionDate(start,end).isEmpty()) {
            return new ArrayList<>();
        }

        return transactionRepository.filterByTransactionDate(start,end);
    }

    public boolean withDrawMoney(Account account, BigDecimal amountToWithdraw) {
        if (!validateToWithdraw(account, amountToWithdraw)) return false;

        account.setBalance(account.getBalance().subtract(amountToWithdraw));
        accountRepository.saveAndFlush(account);

        Transaction transaction = new Transaction();
        transaction.setAmountSent(amountToWithdraw);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSenderAccount(account);

        transactionRepository.save(transaction);
        return true;
    }

    private boolean validateToWithdraw(Account account, BigDecimal amountToWithdraw) {
        if (account == null) {
            return false;
        }

        if (account.getStatusAccount() != StatusAccount.ACTIVE) {
            return false;
        }

        if (amountToWithdraw == null || amountToWithdraw.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (!account.checkIfTheBalanceWillBeNegative(amountToWithdraw)) {
            return false;
        }

        if (amountToWithdraw.compareTo(maximumDailyWithdrawalAmountAllowed) < 0) {
            return false;
        }

        return true;
    }


    //Depositar
    public boolean deposit(Account account, BigDecimal amountToDeposit) {

        if (validateToDeposit(account)) {
            return false;
        }

        account.setBalance(account.getBalance().add(amountToDeposit));
        accountRepository.saveAndFlush(account);

        Transaction transaction = new Transaction();
        transaction.setAmountSent(amountToDeposit);
        transaction.setTransactionType(TransactionType.DEPOSITED);
        transaction.setSenderAccount(account);

        transactionRepository.save(transaction);
        return true;
    }

    public boolean validateToDeposit(Account account) {
        if (account == null) {
            return false;
        }

        if (account.getStatusAccount() != StatusAccount.ACTIVE) {
            return false;
        }

        return true;
    }


}
