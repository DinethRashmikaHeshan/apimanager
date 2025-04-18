package com.example.apimanager.controller;

import com.example.apimanager.config.JwtUtil;
import com.example.apimanager.model.User;
import com.example.apimanager.service.UserDetailsServiceImpl;
import com.example.apimanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") // Specific origin for React frontend
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          UserDetailsServiceImpl userDetailsService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(request.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER");

        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT", description = "Authenticates a user and returns a JWT token and role.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal(); // Cast to User since it implements UserDetails
        String jwt = jwtUtil.generateToken(user);

        // Return both token and role
        return ResponseEntity.ok(new JwtResponse(jwt, user.getRole()));
    }

    @Data
    public static class UserRegistrationRequest {
        private String username;
        private String password;
        private String email;
        private boolean admin;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class JwtResponse {
        private final String token;
        private final String role; // Added role field

        public JwtResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }
    }

    @Data
    public static class MessageResponse {
        private final String message;
    }
}