package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Connection;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @OneToMany(mappedBy = "sender")
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "user")
    private Set<Connection> connections;

}
