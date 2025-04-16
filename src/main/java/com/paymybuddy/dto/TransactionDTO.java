package com.paymybuddy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private String senderEmail;
    private String receiverEmail;
    private Double amount;
    private String description;
}
