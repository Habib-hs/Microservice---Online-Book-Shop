package com.example.authentication.authenticationservice.service;

import com.example.authentication.authenticationservice.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role getRoles(String roleName);
    void addRole(String roleName);
}
