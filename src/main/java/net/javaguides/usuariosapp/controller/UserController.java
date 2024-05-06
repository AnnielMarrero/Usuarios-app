package net.javaguides.usuariosapp.controller;

import jakarta.validation.Valid;
import net.javaguides.usuariosapp.dto.LoginDto;
import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //add user rest api
    @PostMapping
    public ResponseEntity<UserDto> adduser(@RequestBody @Valid UserDto userDto){
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    //get user rest api
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id){
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    //get all users rest api
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/login")
    public boolean login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

}
