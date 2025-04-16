package com.paymybuddy.repository;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByUser(User user);
    Optional<Connection> findByUserAndBuddy(User user, User buddy);
}
