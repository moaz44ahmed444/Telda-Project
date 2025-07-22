package com.telda.telda_project.service;

import com.telda.telda_project.entity.Transaction;
import com.telda.telda_project.entity.User;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(Long id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsBySender(User sender);
    List<Transaction> getTransactionsByReceiver(User receiver);
    List<Transaction> getUserTransactions(User user);

    }
