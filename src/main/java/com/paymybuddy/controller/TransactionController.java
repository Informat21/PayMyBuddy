package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/send")
    public String sendMoney(@RequestBody TransactionDTO dto) {
        return transactionService.createTransaction(dto);
    }
}
