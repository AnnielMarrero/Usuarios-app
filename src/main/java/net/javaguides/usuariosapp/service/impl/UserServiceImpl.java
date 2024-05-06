package net.javaguides.usuariosapp.service.impl;

import net.javaguides.usuariosapp.dto.LoginDto;
import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.entity.User;
import net.javaguides.usuariosapp.exceptions.EmailExistsException;
import net.javaguides.usuariosapp.exceptions.NotFoundException;
import net.javaguides.usuariosapp.mapper.UserMapper;
import net.javaguides.usuariosapp.repository.UserRepository;
import net.javaguides.usuariosapp.service.JwtService;
import net.javaguides.usuariosapp.service.PhoneService;
import net.javaguides.usuariosapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    private final PhoneService phoneService;

    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PhoneService phoneService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.phoneService = phoneService;
        this.jwtService = jwtService;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new EmailExistsException("User's email exists");

        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setModifiedAt(LocalDateTime.now());
        userDto.setActive(true);
        userDto.setLastLoginAt(userDto.getCreatedAt());
        userDto.setToken(jwtService.generateToken(userDto));
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);

        userDto.phones.forEach(currPhone -> currPhone.setUserId(savedUser.getId()));

        List<PhoneDto> phonesDto = phoneService.addRange(userDto.phones); //adding phones to that user
        UserDto userSavedDto = userMapper.mapToUserDto(savedUser);
        userSavedDto.phones = phonesDto;
        return userSavedDto;
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exists"));

        return userMapper.mapToUserDto(user);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public boolean login(LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("User's email does not exists"));
        return passwordEncoder.matches( loginDto.getPassword() , user.getPassword());
    }

}
