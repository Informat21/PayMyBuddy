package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Désactivation de la sécurité pour les tests
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeHttpRequests().anyRequest().permitAll();
            return http.build();
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testShowProfile_Success() throws Exception {
        User user = User.builder()
                .email("user@example.com")
                .username("User")
                .balance(BigDecimal.valueOf(1000.00))
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profil"))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testUpdateProfile_WithPassword() throws Exception {
        User user = User.builder()
                .email("user@example.com")
                .username("OldUser")
                .password("oldPassword")
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");

        mockMvc.perform(post("/profil/update")
                        .param("username", "NewUser")
                        .param("email", "user@example.com")
                        .param("password", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil?success"));

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername().equals("NewUser") &&
                        savedUser.getPassword().equals("encodedNewPass")
        ));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testUpdateProfile_WithoutPassword() throws Exception {
        User user = User.builder()
                .email("user@example.com")
                .username("OldUser")
                .password("oldPassword")
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/profil/update")
                        .param("username", "NewUser")
                        .param("email", "user@example.com")
                        .param("password", "")) // mot de passe vide
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil?success"));

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername().equals("NewUser") &&
                        savedUser.getPassword().equals("oldPassword")
        ));
    }

    @Test
    @WithMockUser(username = "notfound@example.com")
    void testUpdateProfile_UserNotFound() throws Exception {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/profil/update")
                        .param("username", "Test")
                        .param("email", "notfound@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
