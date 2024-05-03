package net.javaguides.usuariosapp.service;

import net.javaguides.usuariosapp.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(UUID id);

    List<UserDto> getAllUsers();

    //void delete(UUID id);
}
