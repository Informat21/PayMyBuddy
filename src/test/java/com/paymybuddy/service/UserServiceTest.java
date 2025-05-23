package com.paymybuddy.service;

import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_success() {
        // Given
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // When
        String result = userService.register(dto);

        // Then
        assertEquals("User registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_emailAlreadyExists() {
        // Given
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When
        String result = userService.register(dto);

        // Then
        assertEquals("Email already in use", result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testFindAllConnections_success() {
        // Given
        String email = "user@example.com";

        User user = User.builder()
                .id(1)
                .email(email)
                .username("MainUser")
                .build();

        User buddy1 = User.builder()
                .id(2)
                .email("buddy1@example.com")
                .username("Buddy One")
                .build();

        User buddy2 = User.builder()
                .id(3)
                .email("buddy2@example.com")
                .username("Buddy Two")
                .build();

        Connection connection1 = Connection.builder().user(user).buddy(buddy1).build();
        Connection connection2 = Connection.builder().user(user).buddy(buddy2).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(connectionRepository.findByUser(user)).thenReturn(List.of(connection1, connection2));

        // When
        var result = userService.findAllConnections(email);

        // Then
        assertEquals(2, result.size());
        assertEquals("buddy1@example.com", result.get(0).getEmail());
        assertEquals("Buddy One", result.get(0).getUsername());
        assertEquals("buddy2@example.com", result.get(1).getEmail());
        assertEquals("Buddy Two", result.get(1).getUsername());

        verify(userRepository, times(1)).findByEmail(email);
        verify(connectionRepository, times(1)).findByUser(user);
    }

}
