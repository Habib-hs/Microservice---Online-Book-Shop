package com.example.authentication.authenticationservice;

import com.example.authentication.authenticationservice.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthenticationserviceApplication {
	@Bean
	CommandLineRunner run(RoleService roleService){
		return args -> {
			roleService.addRole("CUSTOMER");
			roleService.addRole("ADMIN");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationserviceApplication.class, args);
	}

}
