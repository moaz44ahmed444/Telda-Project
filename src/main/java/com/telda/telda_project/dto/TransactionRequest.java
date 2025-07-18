package com.telda.telda_project.dto;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {

    private Long senderId;
    private Long receiverId;
    private Double amount;
    private TransactionType type;
}
