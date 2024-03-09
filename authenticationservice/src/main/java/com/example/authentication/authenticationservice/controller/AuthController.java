package com.example.authentication.authenticationservice.controller;

import com.example.authentication.authenticationservice.model.LoginRequest;
import com.example.authentication.authenticationservice.model.RegisterRequest;
import com.example.authentication.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/api/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterRequest request){
        return new ResponseEntity<>(authService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.loginUser(loginRequest), HttpStatus.OK);
    }
}
