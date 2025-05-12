package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.valueOf(1000.00);

    @OneToMany(mappedBy = "sender")
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "user")
    private Set<Connection> connections;

}
