package com.telda.telda_project.Controller;

import com.telda.telda_project.dto.TransactionResponse;
import com.telda.telda_project.entity.Transaction;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.enums.Role;
import com.telda.telda_project.service.TransactionService;
import com.telda.telda_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final TransactionService transactionService;
    private final UserService userService;

    public AdminController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/transactions/all")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<Transaction>  allTransactions = transactionService.getAllTransactions();

        List<TransactionResponse> responses = allTransactions.stream().map(tx ->
                TransactionResponse.builder()
                        .transactionId(tx.getId())
                        .senderEmail(tx.getSender() != null ? tx.getSender().getEmail() : null)
                        .receiverEmail(tx.getReceiver() != null ? tx.getReceiver().getEmail() : null)
                        .amount(tx.getAmount())
                        .type(tx.getType())
                        .timestamp(tx.getTimestamp())
                        .build()
                ).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        userService.activateUser(id);
        return ResponseEntity.ok("User Activated Successfully");
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id){
        User user = userService.getUserById(id).get();

        if (user.getRole() == Role.ADMIN) {
            return ResponseEntity.status(403).body("Cannot deactivate an Admin user");
        }
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getEmail().equals(currentEmail)) {
            return ResponseEntity.status(403).body("You cannot deactivate your own account");
        }

        userService.deactivateUser(id);
        return ResponseEntity.ok("User Deactivated Successfully");
    }

}
