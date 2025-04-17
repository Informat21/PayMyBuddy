package com.paymybuddy.service;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public String addConnection(ConnectionDTO dto) {
        User user = userRepository.findByEmail(dto.getUserEmail()).orElse(null);
        User buddy = userRepository.findByEmail(dto.getBuddyEmail()).orElse(null);

        if (user == null || buddy == null) {
            return "User or Buddy not found";
        }

        if (connectionRepository.findByUserAndBuddy(user, buddy).isPresent()) {
            return "Connection already exists";
        }

        Connection connection = Connection.builder()
                .user(user)
                .buddy(buddy)
                .build();

        connectionRepository.save(connection);
        return "Connection added";
    }
}
