package com.paymybuddy.controller;


import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthentificationController.class)
public class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
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
    /*
     Test : Affiche le formulaire d'inscription
     */
    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    /*
     * Test : Inscription réussie => redirection vers /auth/login
     */
    @Test
    void testRegisterSuccess() throws Exception {
        Mockito.when(userService.register(any(UserRegistrationDTO.class)))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/auth/register")
                        .param("email", "test@example.com")
                        .param("password", "testpass")
                        .param("username", "testuser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    /*
     *  Test : Inscription échouée => vue "register" avec message d'erreur
     */
    @Test
    void testRegisterFailure() throws Exception {
        Mockito.when(userService.register(any(UserRegistrationDTO.class)))
                .thenReturn("Email already in use");

        mockMvc.perform(post("/auth/register")
                        .param("email", "test@example.com")
                        .param("password", "testpass")
                        .param("username", "testuser"))

                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Email already in use"));
    }

    /*
     *  Test : Affiche le formulaire de connexion
     */
    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginDTO"));
    }

    /*
     *  Test : Connexion simulée => redirection vers /home
     */
    @Test
    void testLoginPost() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("email", "test@example.com")
                        .param("password", "testpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
