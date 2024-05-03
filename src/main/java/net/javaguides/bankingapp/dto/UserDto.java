package net.javaguides.bankingapp.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    private String name;
    private String email;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime lastLoginAt;

    private String token;
    private boolean isActive;
}
