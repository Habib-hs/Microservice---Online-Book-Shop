package com.example.authentication.authenticationservice.config;

import com.example.authentication.authenticationservice.entity.UserEntity;
import com.example.authentication.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig implements UserDetailsService{
    private final UserRepository userRepo;

    //    @Bean
//    public UserDetailsService userDetailsService(){
//        return username -> userRepo.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//    }
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserEntity> user = userRepo.findByEmail(username);
        if(user.isPresent()){
            UserEntity requiredUser = user.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            requiredUser.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            });
            return new org.springframework.security.core.userdetails.User(requiredUser.getEmail(), requiredUser.getPassword(),authorities);
        }
        else{
            throw new UsernameNotFoundException("USER IS NOT FOUND");
        }
    }

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this::loadUserByUsername);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
