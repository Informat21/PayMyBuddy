/*package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;

    public List<Connection> getConnectionsForUser(User user) {
        return connectionRepository.findByUser(user);
    }

    public boolean areConnected(User user, User buddy) {
        return connectionRepository.findByUserAndBuddy(user, buddy).isPresent();
    }

    public Connection addConnection(User user, User buddy) {
        Connection connection = Connection.builder()
                .user(user)
                .buddy(buddy)
                .build();
        return connectionRepository.save(connection);
    }
}

 */
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
