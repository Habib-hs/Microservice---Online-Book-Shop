package com.example.authentication.authenticationservice.service;

import com.example.authentication.authenticationservice.model.AuthenticationResponse;
import com.example.authentication.authenticationservice.model.LoginRequest;
import com.example.authentication.authenticationservice.model.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String registerUser(RegisterRequest request);
    AuthenticationResponse loginUser(LoginRequest loginRequest);
}
