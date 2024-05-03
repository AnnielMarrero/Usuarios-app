package net.javaguides.bankingapp.service.impl;

import net.javaguides.bankingapp.dto.UserDto;
import net.javaguides.bankingapp.entity.User;
import net.javaguides.bankingapp.mapper.UserMapper;
import net.javaguides.bankingapp.repository.UserRepository;
import net.javaguides.bankingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(UUID id) {
        User User = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exists"));

        return userMapper.mapToUserDto(User);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> Users = userRepository.findAll();
        return Users.stream().map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
    /*
    @Override
    public void delete(Long id) {
        User User = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exists"));
       userRepository.deleteById(id);
    }
    */

}
