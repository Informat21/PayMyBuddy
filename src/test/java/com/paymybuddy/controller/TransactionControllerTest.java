package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.argThat;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
            return http.build();
        }

        @Bean
        public TransactionService transactionService() {
            return Mockito.mock(TransactionService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public ConnectionService connectionService() {
            return Mockito.mock(ConnectionService.class);
        }
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testSendMoney_Success() throws Exception {
        doNothing().when(transactionService).createTransaction(any(TransactionDTO.class));

        mockMvc.perform(post("/transactions/send")
                        .param("senderEmail", "user@example.com")
                        .param("receiverEmail", "buddy@example.com")
                        .param("receiverUsername", "Receiver")
                        .param("amount", "100")
                        .param("description", "Test transaction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/transfer"));

        verify(transactionService, times(1)).createTransaction(argThat(dto ->
                dto.getSenderEmail().equals("user@example.com") &&
                        dto.getReceiverEmail().equals("buddy@example.com") &&
                        dto.getReceiverUsername().equals("Receiver") &&
                        dto.getAmount().compareTo(BigDecimal.valueOf(100)) == 0 &&
                        dto.getDescription().equals("Test transaction")
        ));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testSendMoney_Failure() throws Exception {
        doThrow(new IllegalArgumentException("Insufficient funds"))
                .when(transactionService).createTransaction(any(TransactionDTO.class));

        mockMvc.perform(post("/transactions/send")
                        .param("senderEmail", "user@example.com")
                        .param("receiverEmail", "buddy@example.com")
                        .param("receiverUsername", "Receiver")
                        .param("amount", "100")
                        .param("description", "Test transaction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/transactions/transfer?error=Insufficient funds"));

        verify(transactionService, times(2)).createTransaction(any(TransactionDTO.class));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testShowTransferPage() throws Exception {
        User user = User.builder()
                .email("user@example.com")
                .username("User")
                .balance(BigDecimal.valueOf(1000.00))
                .build();

        UserDTO userDTO = new UserDTO(1, "user@example.com", "User", BigDecimal.valueOf(1000.00));

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userService.getUserDTO(user)).thenReturn(userDTO);
        when(transactionService.findAllBySender("user@example.com"))
                .thenReturn(List.of(new TransactionDTO("user@example.com", "buddy@example.com", "Receiver", BigDecimal.valueOf(100), "Test transaction")));
        when(userService.findAllConnections("user@example.com"))
                .thenReturn(List.of(new UserDTO(1, "buddy@example.com", "Buddy", BigDecimal.valueOf(1000.00))));

        mockMvc.perform(get("/transactions/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions"))
                .andExpect(model().attributeExists("currentUser", "buddies", "transactions"));

        verify(userService).findByEmail("user@example.com");
        verify(userService).getUserDTO(user);
        verify(transactionService).findAllBySender("user@example.com");
        verify(userService).findAllConnections("user@example.com");
    }
}
