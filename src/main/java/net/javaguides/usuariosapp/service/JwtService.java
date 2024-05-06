package net.javaguides.usuariosapp.service;


import net.javaguides.usuariosapp.dto.UserDto;

public interface JwtService {
    String generateToken(UserDto userDto);
}