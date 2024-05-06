package net.javaguides.usuariosapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceImplTest {

    @Test
    public void getUserById_ReturnUser(){
        //arrange
        String rawPassword = "hola";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //act
        String encodePassword = passwordEncoder.encode(rawPassword);
        //System.out.println(encodePassword);

        //assert
        boolean matched = passwordEncoder.matches(rawPassword, encodePassword);
        assertTrue(matched);
    }
}
