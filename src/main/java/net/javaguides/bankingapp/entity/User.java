package net.javaguides.bankingapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    @Email
    @Column(unique=true)
    private String email;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime lastLoginAt;

    private String token;
    private boolean isActive;

}
