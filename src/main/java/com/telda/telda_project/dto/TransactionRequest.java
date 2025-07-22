package com.telda.telda_project.dto;
import com.telda.telda_project.enums.TransactionType;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;


@Data
public class TransactionRequest {

    @NotNull(message = "Receiver Id Not valid")
    private Long receiverId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

    private TransactionType type;
}
