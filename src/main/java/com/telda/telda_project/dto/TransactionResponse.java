package com.telda.telda_project.dto;

import com.telda.telda_project.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long transactionId;
    private String senderEmail;
    private String receiverEmail;
    private Double amount;
    private TransactionType type;
    private LocalDateTime timestamp;
}