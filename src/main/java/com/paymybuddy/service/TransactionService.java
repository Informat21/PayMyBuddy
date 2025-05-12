package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    @Transactional
    public void createTransaction(TransactionDTO dto) {
        User sender = userRepository.findByEmail(dto.getSenderEmail())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable : " + dto.getSenderEmail()));
        User receiver = userRepository.findByEmail(dto.getReceiverEmail())
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable : " + dto.getReceiverEmail()));

        BigDecimal amount = dto.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro");
        }

        // Initialisation des soldes si null
        if (sender.getBalance() == null) sender.setBalance(BigDecimal.ZERO);
        if (receiver.getBalance() == null) receiver.setBalance(BigDecimal.ZERO);

        // Vérifie les fonds
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Fonds insuffisants pour effectuer le transfert");
        }

        // Effectue le transfert
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Enregistre la transaction
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .description(dto.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // Sauvegarde les nouveaux soldes
        userRepository.save(sender);
        userRepository.save(receiver);
    }


//    @Transactional
//    public String createTransaction(TransactionDTO dto) {
//        User sender = userRepository.findByEmail(dto.getSenderEmail()).orElse(null);
//        User receiver = userRepository.findByEmail(dto.getReceiverEmail()).orElse(null);
//
//        if (sender == null || receiver == null) {
//            throw new IllegalArgumentException("Sender or Receiver not found");
//        }
//
//        BigDecimal amount = dto.getAmount();
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            return "Amount must be greater than zero";
//        }
//
//        // Vérifie que le solde est suffisant
//        if (sender.getBalance().compareTo(amount) < 0) {
//            return "Fonds insuffisants";
//        }
//
//        // Déduction et crédit
//        sender.setBalance(sender.getBalance().subtract(amount));
//        receiver.setBalance(receiver.getBalance().add(amount));
//
//        Transaction transaction = Transaction.builder()
//                .sender(sender)
//                .receiver(receiver)
//                .amount(dto.getAmount())
//                .description(dto.getDescription())
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        transactionRepository.save(transaction);
//
//        // Sauvegarde des soldes mis à jour
//        userRepository.save(sender);
//        userRepository.save(receiver);
//
//        return "Transaction successful";
//    }

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
