package net.javaguides.usuariosapp.service.impl;

import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.entity.User;
import net.javaguides.usuariosapp.mapper.UserMapper;
import net.javaguides.usuariosapp.repository.PhoneRepository;
import net.javaguides.usuariosapp.repository.UserRepository;
import net.javaguides.usuariosapp.service.PhoneService;
import net.javaguides.usuariosapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;


    @Autowired
    private UserMapper userMapper;


    private PhoneService phoneService;

    public UserServiceImpl(UserRepository userRepository, PhoneService phoneService) {
        this.userRepository = userRepository;
        this.phoneService = phoneService;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("User's email exits");

        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setModifiedAt(LocalDateTime.now());
        userDto.setActive(true);
        userDto.setLastLoginAt(userDto.getCreatedAt());
        userDto.setToken("token"); //TODO generate JWT
        userDto.setPassword(encodePassword(userDto.getPassword()));

        User user = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);

        for (PhoneDto currPhone : userDto.phones)
            currPhone.setUserId(savedUser.getId());

        List<PhoneDto> phonesDto = phoneService.addRange(userDto.phones); //adding phones to that user
        UserDto userSavedDto = userMapper.mapToUserDto(savedUser);
        userSavedDto.phones = phonesDto;
        return userSavedDto;
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exists"));

        return userMapper.mapToUserDto(user);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public static String encodePassword(String plainPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(plainPassword);
    }

}
