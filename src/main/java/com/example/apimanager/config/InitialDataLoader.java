// src/main/java/com/example/apimanager/config/InitialDataLoader.java
package com.example.apimanager.config;

import com.example.apimanager.model.User;
import com.example.apimanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class InitialDataLoader {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                Long userCount = userRepository.count();
                System.out.println("Current user count: " + userCount);

                if (userCount == 0) {
                    createUsers(userRepository, passwordEncoder);
                } else {
                    System.out.println("Users already exist, skipping initialization");
                }
            } catch (Exception e) {
                System.err.println("Error initializing data: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    @Transactional
    public void createUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setRole("ROLE_ADMIN");
        userRepository.save(admin);

        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("user123"));
        regularUser.setEmail("user@example.com");
        regularUser.setRole("ROLE_USER");
        userRepository.save(regularUser);

        System.out.println("Initial users created");
    }
}