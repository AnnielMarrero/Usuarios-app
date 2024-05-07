package net.javaguides.usuariosapp;

import com.google.gson.Gson;
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
import net.javaguides.usuariosapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.SerializationUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneService phoneService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserMapper userMapperReal;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, phoneService, jwtService);
        ReflectionTestUtils.setField(userService, "userMapper", userMapper);

        userMapperReal = new UserMapper();
        ReflectionTestUtils.setField(userMapperReal, "modelMapper", new ModelMapper());
        ReflectionTestUtils.setField(userService, "passwordEncoder",passwordEncoder);
    }

    @Test
    public void getUserByIdShouldReturnUserDtoFromService(){
        //arrange
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setName("Laura");
        user.setEmail("laura@gmail.com");

        Mockito.when(userMapper.mapToUserDto(user)).thenReturn(userMapperReal.mapToUserDto(user));
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //act
        var userDto = userService.getUserById(id);

        //assert
        assertEquals(id, userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());

    }

    @Test
    public void getUserByIdShouldThrowNotFoundExceptionFromService(){
        //arrange
        String msg = "User does not exists";
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findById(id)).thenThrow(new NotFoundException(msg));
        //act
        try{
            userService.getUserById(id);
        } catch (Exception ex){
            //assert
            assertEquals(NotFoundException.class, ex.getClass() );
            assertEquals(msg, ex.getMessage());
        }
    }

    @Test
    public void getAllUsersShouldReturnEmptyUsersFromService(){
        //arrange
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());
        //act
        var usersDto = userService.getAllUsers();
        //assert
        assertEquals(0, usersDto.size());

    }

    @Test
    public void getAllUsersShouldReturnUsersFromService(){
        //arrange

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("Laura");
        user1.setEmail("laura@gmail.com");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Rosy");
        user2.setEmail("rosy@gmail.com");

        List<User> users = List.of(user1, user2);
        users.forEach(user ->
                Mockito.when(userMapper.mapToUserDto(user)).thenReturn(userMapperReal.mapToUserDto(user)));
        Mockito.when(userRepository.findAll()).thenReturn(users);

        //act
        var usersDto = userService.getAllUsers();
        //assert
        assertEquals(users.size(), usersDto.size());
        assertEquals(users.getFirst().getName(), usersDto.getFirst().getName());
        assertEquals(users.getFirst().getEmail(), usersDto.getFirst().getEmail());
    }

    @Test
    public void createUserShouldThrowEmailExistsExceptionFromService(){
        //arrange
        String msg = "El correo ya registrado";
        Optional<User> user = Optional.of(new User());
        UserDto userDto = new UserDto();
        userDto.setEmail("laura@gmail.com");
        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(user);

        //act
        try{
            userService.createUser(userDto);
        } catch (Exception ex){
            //assert
            assertEquals(EmailExistsException.class, ex.getClass() );
            assertEquals(msg, ex.getMessage());
        }
    }

    @Test
    public void createUserShouldReturnUserDtoFromService(){
        //arrange
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(UUID.randomUUID());
        phoneDto.setNumber("53151515");
        phoneDto.setCountryCode("53");
        phoneDto.setCityCode("1");
        List<PhoneDto> phonesDto = List.of(phoneDto);

        UserDto userDto = new UserDto();
        userDto.setName("Laura");
        userDto.setEmail("laura@gmail.com");
        userDto.setPassword("123AAaa");
        userDto.setPhones(phonesDto);

        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn(UUID.randomUUID().toString()); //every time will genrate different value

        User user = userMapperReal.mapToUser(userDto);
        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setActive(true);
        user.setLastLoginAt(user.getCreatedAt());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setToken("token");

        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userMapper.mapToUser(userDto)).thenReturn(user);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(phoneService.addRange(userDto.phones)).thenReturn(phonesDto);
        Mockito.when(userMapper.mapToUserDto(user)).thenReturn(userMapperReal.mapToUserDto(user));
        //act
        UserDto userSavedDto = userService.createUser(userDto);

        //assert
        assertEquals(user.getId(), userSavedDto.getId() );
        assertEquals(userDto.getName(), userSavedDto.getName() );
        assertEquals(userDto.getEmail(), userSavedDto.getEmail() );
        assertNotEquals(userDto.getPassword(), userSavedDto.getPassword() );
        assertNull(userSavedDto.getPassword());
        assertEquals(userDto.getPhones().size(), userSavedDto.getPhones().size());
        assertEquals(userDto.getPhones().getFirst().getNumber(), userSavedDto.getPhones().getFirst().getNumber());
        assertTrue(userSavedDto.isActive());
        assertFalse(userSavedDto.getToken().isEmpty());
        int cmp = userSavedDto.getCreatedAt().compareTo(LocalDateTime.now());
        assertTrue(cmp <= 0); //less or equal
    }
}
