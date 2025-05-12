package com.paymybuddy.service;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return "Email already in use";
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .balance(BigDecimal.valueOf(1000.00)) // Default balance
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    public UserDTO getUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId().intValue());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDTO> findAllConnections(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Connection> connections = connectionRepository.findByUser(user);
        return connections.stream()
                .map(Connection::getBuddy)
                .map(this::getUserDTO)
                .collect(Collectors.toList());
    }
}
