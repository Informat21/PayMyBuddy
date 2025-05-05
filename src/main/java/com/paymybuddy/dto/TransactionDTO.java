package com.paymybuddy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private String senderEmail;
    private String receiverEmail;
    private String receiverUsername;
    private BigDecimal amount;
    private String description;
}
