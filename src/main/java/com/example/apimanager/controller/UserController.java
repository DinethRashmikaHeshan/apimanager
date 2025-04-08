//package com.example.apimanager.controller;
//
//import com.example.apimanager.model.User;
//import com.example.apimanager.service.UserService;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/users")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//public class UserController {
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers() {
//        List<UserResponse> users = userService.getAllUsers().stream()
//                .map(this::mapUserToResponse)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//        return ResponseEntity.ok(mapUserToResponse(user));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
//        User userDetails = new User();
//        userDetails.setUsername(request.getUsername());
//        userDetails.setEmail(request.getEmail());
//        userDetails.setPassword(request.getPassword());
//        userDetails.setRole(request.getRole());
//
//        User updatedUser = userService.updateUser(id, userDetails);
//        return ResponseEntity.ok(mapUserToResponse(updatedUser));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
//    }
//
//    private UserResponse mapUserToResponse(User user) {
//        UserResponse response = new UserResponse();
//        response.setId(user.getId());
//        response.setUsername(user.getUsername());
//        response.setEmail(user.getEmail());
//        response.setRole(user.getRole());
//        return response;
//    }
//
//    @Data
//    public static class UserResponse {
//        private Long id;
//        private String username;
//        private String email;
//        private String role;
//    }
//
//    @Data
//    public static class UserUpdateRequest {
//        private String username;
//        private String email;
//        private String password;
//        private String role;
//    }
//
//    @Data
//    public static class MessageResponse {
//        private final String message;
//    }
//}


package com.example.apimanager.controller;

import com.example.apimanager.model.User;
import com.example.apimanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@SecurityRequirement(name = "BearerAuth") // Requires JWT authentication
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all registered users (admin only).")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Fetches details of a specific user by ID (admin only).")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapUserToResponse(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates details of a specific user by ID (admin only).")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        User userDetails = new User();
        userDetails.setUsername(request.getUsername());
        userDetails.setEmail(request.getEmail());
        userDetails.setPassword(request.getPassword());
        userDetails.setRole(request.getRole());

        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(mapUserToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a specific user by ID (admin only).")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    private UserResponse mapUserToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String role;
    }

    @Data
    public static class UserUpdateRequest {
        private String username;
        private String email;
        private String password;
        private String role;
    }

    @Data
    public static class MessageResponse {
        private final String message;
    }
}