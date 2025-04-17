package com.paymybuddy.service;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return "Email already in use";
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
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
}
