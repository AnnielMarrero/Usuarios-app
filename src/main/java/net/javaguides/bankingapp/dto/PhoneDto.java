package net.javaguides.bankingapp.dto;

import lombok.Data;

@Data
public class PhoneDto {
    private String number;
    private String cityCode;
    private String contryCode;
}
