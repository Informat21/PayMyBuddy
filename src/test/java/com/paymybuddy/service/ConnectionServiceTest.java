package com.paymybuddy.service;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionServiceTest {

    private ConnectionRepository connectionRepository;
    private UserRepository userRepository;
    private ConnectionService connectionService;

    @BeforeEach
    void setUp() {
        connectionRepository = mock(ConnectionRepository.class);
        userRepository = mock(UserRepository.class);
        connectionService = new ConnectionService(connectionRepository, userRepository);
    }

    @Test
    void testAddConnection_Success() {
        // Arrange
        ConnectionDTO dto = new ConnectionDTO("user@example.com", "buddy@example.com");

        User user = User.builder().id(1).email("user@example.com").build();
        User buddy = User.builder().id(2).email("buddy@example.com").build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("buddy@example.com")).thenReturn(Optional.of(buddy));
        when(connectionRepository.findByUserAndBuddy(user, buddy)).thenReturn(Optional.empty());

        // Act
        String result = connectionService.addConnection(dto);

        // Assert
        assertEquals("Connection added", result);
        verify(connectionRepository).save(any(Connection.class));
    }

    @Test
    void testAddConnection_UserNotFound() {
        // Arrange
        ConnectionDTO dto = new ConnectionDTO("user@example.com", "buddy@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("buddy@example.com")).thenReturn(Optional.of(new User()));

        // Act
        String result = connectionService.addConnection(dto);

        // Assert
        assertEquals("User or Buddy not found", result);
        verify(connectionRepository, never()).save(any());
    }

    @Test
    void testAddConnection_ConnectionAlreadyExists() {
        // Arrange
        ConnectionDTO dto = new ConnectionDTO("user@example.com", "buddy@example.com");

        User user = User.builder().id(1).email("user@example.com").build();
        User buddy = User.builder().id(2).email("buddy@example.com").build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("buddy@example.com")).thenReturn(Optional.of(buddy));
        when(connectionRepository.findByUserAndBuddy(user, buddy)).thenReturn(Optional.of(new Connection()));

        // Act
        String result = connectionService.addConnection(dto);

        // Assert
        assertEquals("Connection already exists", result);
        verify(connectionRepository, never()).save(any());
    }
}
