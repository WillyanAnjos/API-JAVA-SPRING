package br.com.ditwowin.api.repositories;

import br.com.ditwowin.api.models.Account;
import br.com.ditwowin.api.models.Transaction;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :start AND :end AND t.senderAccount = :senderAccount")
    List<Transaction> filterByTransactionDate(Account senderAccount, LocalDateTime start, LocalDateTime end);

}