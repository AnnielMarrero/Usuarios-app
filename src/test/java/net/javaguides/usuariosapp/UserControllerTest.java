package net.javaguides.usuariosapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.usuariosapp.controller.UserController;
import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.exceptions.EmailExistsException;
import net.javaguides.usuariosapp.exceptions.NotFoundException;
import net.javaguides.usuariosapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserController userController;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void getUserByIdShouldReturnUserDto(){
        //arrange
        UUID id = UUID.randomUUID();
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName("Laura");
        userDto.setEmail("laura@gmail.com");

        Mockito.when(userService.getUserById(id)).thenReturn(userDto);
        //act
        ResponseEntity<UserDto> responseEntity = userController.getUserById(id);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDto.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
    }
    //TODO pending
    @Test
    public void getUserByIdShouldReturnBadRequest() throws Exception {
        //arrange
        String msg = "User does not exists";
        UUID id = UUID.randomUUID();
        Mockito.when(userService.getUserById(id)).thenThrow(new NotFoundException(msg));

        //act
        MvcResult result = mockMvc.perform(get("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNotFound()) //assert 400
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        //System.out.println(content);
        Map<String,List<String>> map = objectMapper.readValue(content, Map.class);
        var errorList = map.get("mensaje");
        assertTrue(errorList.contains("User does not exists"));

    }

    @Test
    public void getAllUsersShouldReturnEmptyUsers(){
        //arrange
        Mockito.when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        //act
        ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, Objects.requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    public void getAllUsersShouldReturnUsers(){
        //arrange
        var user1 = new UserDto();
        user1.setId(UUID.randomUUID());
        user1.setName("Laura");
        user1.setEmail("laura@gmail.com");

        var user2 = new UserDto();
        user2.setId(UUID.randomUUID());
        user2.setName("Rosy");
        user2.setEmail("rosy@gmail.com");

        var users = List.of(user1, user2);
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        //act
        var responseEntity = userController.getAllUsers();
        //assert
        var usersDto = Objects.requireNonNull(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users.size(), usersDto.size());
        assertEquals(users.getFirst().getName(), usersDto.getFirst().getName());
        assertEquals(users.getFirst().getEmail(), usersDto.getFirst().getEmail());
    }

    @Test
    public void createUserShouldReturnBadRequestWhenEmailExists() throws Exception {
        //arrange
        String msg = "El correo ya registrado";
        var userDto = new UserDto();
        userDto.setName("Laura");
        userDto.setEmail("laura@gmail.com");
        userDto.setPassword("rAAs22");
        userDto.setPhones(new ArrayList<>());
        Mockito.when(userService.createUser(userDto)).thenThrow(new EmailExistsException(msg));
        //act
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //assert 400
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        //System.out.println(content);
        Map<String,List<String>> map = objectMapper.readValue(content, Map.class);
        var errorList = map.get("mensaje");
        //errorList.forEach(System.out::println);
        assertEquals(1, errorList.size());
        assertTrue(errorList.contains(msg));
    }

    @Test
    public void createUserShouldReturnUserDto(){
        //arrange
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setNumber("53151515");
        phoneDto.setCountryCode("53");
        phoneDto.setCityCode("1");
        List<PhoneDto> phonesDto = List.of(phoneDto);

        UserDto userDto = new UserDto();
        userDto.setName("Laura");
        userDto.setEmail("laura@gmail.com");
        userDto.setPassword("123AAaa");
        userDto.setPhones(phonesDto);

        Mockito.when(userService.createUser(userDto)).thenReturn(userDto);
        //act
        ResponseEntity<UserDto> responseEntity = userController.adduser(userDto);

        //assert
        var userCreated = Objects.requireNonNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userDto.getName(), userCreated.getName());
        assertEquals(userDto.getId(), userCreated.getId());
    }

    @Test
    public void createUserShouldReturnBadRequestWhenBadInput() throws Exception {
        //arrange
        var userDto = new UserDto();
        userDto.setName("Laura");
        userDto.setEmail("lauragmail.com"); //email not valid
        userDto.setPassword("r"); //password not valid
        userDto.setPhones(new ArrayList<>());

        //act
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //assert 400
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        //System.out.println(content);
        Map<String,List<String>> map = objectMapper.readValue(content, Map.class);
        var errorList = map.get("mensaje");
        //errorList.forEach(System.out::println);
        assertEquals(2, errorList.size());
        assertTrue(errorList.contains("Invalid email"));
    }
}
