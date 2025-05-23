package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        userRepository = mock(UserRepository.class);
        transactionService = new TransactionService(transactionRepository, userRepository);
    }

    @Test
    void testCreateTransaction_success() {
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";

        User sender = User.builder()
                .id(1)
                .email(senderEmail)
                .balance(BigDecimal.valueOf(500))
                .build();

        User receiver = User.builder()
                .id(2)
                .email(receiverEmail)
                .balance(BigDecimal.valueOf(200))
                .build();

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderEmail(senderEmail);
        dto.setReceiverEmail(receiverEmail);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setDescription("Test transfer");

        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));

        transactionService.createTransaction(dto);

        // Vérification que les soldes ont été mis à jour
        assertEquals(BigDecimal.valueOf(400), sender.getBalance());
        assertEquals(BigDecimal.valueOf(300), receiver.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);
    }

    @Test
    void testCreateTransaction_receiverNotFound() {
        String senderEmail = "sender@example.com";
        String receiverEmail = "unknown@example.com";

        User sender = User.builder()
                .id(1)
                .email(senderEmail)
                .balance(BigDecimal.valueOf(1000))
                .build();

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderEmail(senderEmail);
        dto.setReceiverEmail(receiverEmail);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setDescription("Test");

        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(dto);
        });

        assertEquals("Destinataire introuvable : " + receiverEmail, exception.getMessage());

        verify(userRepository).findByEmail(senderEmail);
        verify(userRepository).findByEmail(receiverEmail);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_insufficientFunds() {
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";

        User sender = User.builder()
                .id(1)
                .email(senderEmail)
                .balance(BigDecimal.valueOf(50))
                .build();

        User receiver = User.builder()
                .id(2)
                .email(receiverEmail)
                .balance(BigDecimal.valueOf(200))
                .build();

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderEmail(senderEmail);
        dto.setReceiverEmail(receiverEmail);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setDescription("Test");

        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(dto);
        });

        assertEquals("Fonds insuffisants pour effectuer le transfert", exception.getMessage());

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_invalidAmount() {
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";

        User sender = User.builder()
                .id(1)
                .email(senderEmail)
                .balance(BigDecimal.valueOf(500))
                .build();

        User receiver = User.builder()
                .id(2)
                .email(receiverEmail)
                .balance(BigDecimal.valueOf(300))
                .build();

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderEmail(senderEmail);
        dto.setReceiverEmail(receiverEmail);
        dto.setAmount(BigDecimal.ZERO); // montant invalide
        dto.setDescription("Test");

        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(dto);
        });

        assertEquals("Le montant doit être supérieur à zéro", exception.getMessage());

        verifyNoInteractions(transactionRepository);
    }
}
