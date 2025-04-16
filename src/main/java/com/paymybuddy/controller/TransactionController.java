/*package com.paymybuddy.controller;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(
            @RequestParam String senderEmail,
            @RequestParam String receiverEmail,
            @RequestParam BigDecimal amount,
            @RequestParam String description
    ) {
        User sender = userService.findByEmail(senderEmail).orElse(null);
        User receiver = userService.findByEmail(receiverEmail).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("Sender or receiver not found");
        }

        Transaction transaction = transactionService.sendTransaction(sender, receiver, amount, description);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<Transaction>> getSentTransactions(@RequestParam String senderEmail) {
        User sender = userService.findByEmail(senderEmail).orElse(null);
        if (sender == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(transactionService.getSentTransactions(sender));
    }

    @GetMapping("/received")
    public ResponseEntity<List<Transaction>> getReceivedTransactions(@RequestParam String receiverEmail) {
        User receiver = userService.findByEmail(receiverEmail).orElse(null);
        if (receiver == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(transactionService.getReceivedTransactions(receiver));
    }
}


 */
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
