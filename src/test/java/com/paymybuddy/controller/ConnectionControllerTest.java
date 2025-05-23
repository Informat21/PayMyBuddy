package com.paymybuddy.controller;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.service.ConnectionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConnectionController.class)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConnectionService connectionService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConnectionService connectionService() {
            return Mockito.mock(ConnectionService.class);
        }
    }

    // Désactivation de la sécurité pour les tests
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeHttpRequests().anyRequest().permitAll();
            return http.build();
        }
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testAddConnection_Success() throws Exception {
        // Arrange
        when(connectionService.addConnection(any(ConnectionDTO.class)))
                .thenReturn("Connection added");

        // Act & Assert
        mockMvc.perform(post("/connections/add")
                        .param("email", "buddy@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/transfer"))
                .andExpect(flash().attribute("success", "Relation ajoutée avec succès !"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testAddConnection_Failure() throws Exception {
        when(connectionService.addConnection(any(ConnectionDTO.class)))
                .thenReturn("Connection already exists");

        mockMvc.perform(post("/connections/add")
                        .param("email", "buddy@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/transfer"))
                .andExpect(flash().attribute("error", "Connection already exists"));
    }

    @Test
    void testShowAddConnectionPage() throws Exception {
        mockMvc.perform(get("/connections/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addConnection"));
    }
}
