package com.telda.telda_project.serviceImpl;

import com.telda.telda_project.entity.Transaction;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.repository.TransactionRepository;
import com.telda.telda_project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsBySender(User sender) {
        return transactionRepository.findBySender(sender);
    }

    @Override
    public List<Transaction> getTransactionsByReceiver(User receiver) {
        return transactionRepository.findByReceiver(receiver);
    }

    public  List<Transaction> getUserTransactions(User user) {
        List<Transaction> sent = transactionRepository.findBySender(user);
        List<Transaction> received = transactionRepository.findByReceiver(user);

        List<Transaction> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);

        return all;
    }
}
