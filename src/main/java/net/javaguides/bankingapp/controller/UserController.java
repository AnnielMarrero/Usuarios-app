package net.javaguides.bankingapp.controller;

import jakarta.validation.Valid;
import net.javaguides.bankingapp.dto.UserDto;
import net.javaguides.bankingapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    /*
    //delete users rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteuser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok("user is deleted successfully!");
    }

     */
}
