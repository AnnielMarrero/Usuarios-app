package net.javaguides.usuariosapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    @NotBlank(message = "Invalid Name: Empty name")
    @NotNull(message = "Invalid Name: Name is NULL")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Invalid password: Empty password")
    @NotNull(message = "Invalid password: Password is NULL")
    //@Pattern(regexp = "^\\d{10}$", message = "Invalid password") //TODO pendiente validar: Una Mayuscula, letras minúsculas, y dos numeros)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime lastLoginAt;

    private String token;
    private boolean isActive;

    public List<PhoneDto> phones;
}
