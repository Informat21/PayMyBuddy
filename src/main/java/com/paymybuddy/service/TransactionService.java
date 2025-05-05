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
import java.util.List;
import java.util.stream.Collectors;

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
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        return "Transaction successful";
    }

    public List<TransactionDTO> findAllBySender(String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Transaction> transactions = transactionRepository.findBySender(sender);

        return transactions.stream().map(tx -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setSenderEmail(sender.getEmail());
            dto.setReceiverEmail(tx.getReceiver().getEmail());
            dto.setReceiverUsername(tx.getReceiver().getUsername());
            dto.setAmount(tx.getAmount());
            dto.setDescription(tx.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
}
