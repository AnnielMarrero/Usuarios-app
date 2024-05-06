package net.javaguides.usuariosapp.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImp implements JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private String jwtExpiration;

    public String generateToken(UserDto userDto) {
        return Jwts
                .builder()
                .setSubject(userDto.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpiration)))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }



}