package com.paymybuddy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String username;
    private BigDecimal balance;
}
