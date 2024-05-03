package net.javaguides.usuariosapp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PhoneDto {
    private UUID id;
    private String number;
    private String cityCode;
    private String contryCode;
    private UUID userId;
}
