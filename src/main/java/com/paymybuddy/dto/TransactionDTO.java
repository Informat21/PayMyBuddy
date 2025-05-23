package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
    private String senderEmail;
    private String receiverEmail;
    private String receiverUsername;
    private BigDecimal amount;
    private String description;


}
