package com.telda.telda_project.Controller;

import com.telda.telda_project.dto.TransactionRequest;
import com.telda.telda_project.dto.TransactionResponse;
import com.telda.telda_project.entity.Transaction;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.enums.TransactionType;
import com.telda.telda_project.service.TransactionService;
import com.telda.telda_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;



    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> sendMoney(@RequestBody TransactionRequest request) {
        Optional<User> senderOpt = userService.getUserById(request.getSenderId());
        Optional<User> receiverOpt = userService.getUserById(request.getReceiverId());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender or Receiver not found");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than 0");
        }

        if (request.getSenderId().equals(request.getReceiverId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender and Receiver cannot be the same");
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        if (sender.getBalance() < request.getAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        userService.saveUser(sender);
        userService.saveUser(receiver);

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.TRANSFER)
                .build();

        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok("Transaction sent successfully");
    }

    /*
    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return transactionService.getTransactionsBySender(user.get());
    }*/

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Transaction> transactions = transactionService.getTransactionsBySender(user.get());

        List<TransactionResponse> response = transactions.stream().map(tx ->
                TransactionResponse.builder()
                        .transactionId(tx.getId())
                        .senderId(tx.getSender().getId())
                        .receiverId(tx.getReceiver().getId())
                        .amount(tx.getAmount())
                        .type(tx.getType())
                        .timestamp(tx.getTimestamp())
                        .build()
        ).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{suerId}/all")
    public ResponseEntity<List<TransactionResponse>> getAllUserTransactions(@PathVariable Long suerId) {
        Optional<User> userOpt = userService.getUserById(suerId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOpt.get();

        List<Transaction> sent = transactionService.getTransactionsBySender(user);
        List<Transaction> received = transactionService.getTransactionsByReceiver(user);

        List<Transaction> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);

        List<TransactionResponse> response = all.stream().map(tx ->
                TransactionResponse.builder()
                        .transactionId(tx.getId())
                        .senderId(tx.getSender().getId())
                        .receiverId(tx.getReceiver().getId())
                        .amount(tx.getAmount())
                        .type(tx.getType())
                        .timestamp(tx.getTimestamp())
                        .build()
        ).toList();

        return ResponseEntity.ok(response);
    }


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest request) {
        Optional<User> userOpt = userService.getUserById(request.getReceiverId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than 0");
        }

        User user = userOpt.get();

        user.setBalance(user.getBalance() + request.getAmount());
        userService.saveUser(user);

        Transaction transaction = Transaction.builder()
                .sender(null)
                .receiver(user)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .build();

        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok("Deposit Added successfully");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest request) {
        Optional<User> userOpt = userService.getUserById(request.getSenderId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than 0");
        }

        User user = userOpt.get();

        if (user.getBalance() < request.getAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Balance");
        }
        user.setBalance(user.getBalance() - request.getAmount());
        userService.saveUser(user);

        Transaction transaction = Transaction.builder()
                .sender(user)
                .receiver(null)
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.WITHDRAW)
                .build();

        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok("Withdraw successful");
    }

}
