/*package com.paymybuddy.service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction sendTransaction(User sender, User receiver, BigDecimal amount, String description) {
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getSentTransactions(User sender) {
        return transactionRepository.findBySender(sender);
    }

    public List<Transaction> getReceivedTransactions(User receiver) {
        return transactionRepository.findByReceiver(receiver);
    }
}


 */
package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public String createTransaction(TransactionDTO dto) {
        User sender = userRepository.findByEmail(dto.getSenderEmail()).orElse(null);
        User receiver = userRepository.findByEmail(dto.getReceiverEmail()).orElse(null);

        if (sender == null || receiver == null) {
            return "Sender or Receiver not found";
        }

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(BigDecimal.valueOf(dto.getAmount()))
                .description(dto.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        return "Transaction successful";
    }
}
