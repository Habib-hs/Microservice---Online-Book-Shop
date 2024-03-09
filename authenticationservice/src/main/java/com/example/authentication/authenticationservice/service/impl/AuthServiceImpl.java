package com.example.authentication.authenticationservice.service.impl;

import com.example.authentication.authenticationservice.entity.Role;
import com.example.authentication.authenticationservice.entity.UserEntity;
import com.example.authentication.authenticationservice.model.AuthenticationResponse;
import com.example.authentication.authenticationservice.model.LoginRequest;
import com.example.authentication.authenticationservice.model.RegisterRequest;
import com.example.authentication.authenticationservice.repository.UserRepository;
import com.example.authentication.authenticationservice.service.AuthService;
import com.example.authentication.authenticationservice.service.RoleService;
import com.example.authentication.authenticationservice.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    @Override
    public String registerUser(RegisterRequest request) {
        Optional<UserEntity> userEntity = userRepo.findByEmail(request.getEmail());
        if (userEntity.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        List<String> roles = request.getRoles();
        List<Role> toBeAddedRoles = new ArrayList<>();
        for (String role : roles) {
            Role requiredRole = roleService.getRoles(role);
            if (requiredRole != null) {
                toBeAddedRoles.add(requiredRole);
            } else {
                throw new RuntimeException("No such roles");
            }

        }
        if (request != null) {
            UserEntity requiredUser = UserEntity.builder()
                    .email(request.getEmail())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))//passwordEncoder.encode
                    .roles(toBeAddedRoles).build();
            userRepo.save(requiredUser);
            return "User is created";

        } else {
            return "All the fields are not added";
        }
    }

    @Override
    public AuthenticationResponse loginUser(LoginRequest loginRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Optional<UserEntity> user = userRepo.findByEmail(loginRequest.getEmail());

        if(user.isPresent()){
            UserEntity requiredUser = user.get();

            var jwtToken = jwtService.generateToken(requiredUser);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        }

        else{
            throw new UsernameNotFoundException("Not found");
        }
    }
}