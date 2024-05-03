package net.javaguides.usuariosapp.service;

import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface PhoneService {
    List<PhoneDto> addRange(List<PhoneDto> phoneDtos);

}
