//package com.example.apimanager.controller;
//
//import com.example.apimanager.model.ApiKey;
//import com.example.apimanager.model.User;
//import com.example.apimanager.service.ApiKeyService;
//import com.example.apimanager.service.ApiUsageService;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/keys")
//public class ApiKeyController {
//    private final ApiKeyService apiKeyService;
//    private final ApiUsageService apiUsageService;
//
//    public ApiKeyController(ApiKeyService apiKeyService, ApiUsageService apiUsageService) {
//        this.apiKeyService = apiKeyService;
//        this.apiUsageService = apiUsageService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ApiKeyResponse>> getMyApiKeys(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        List<ApiKeyResponse> apiKeys = apiKeyService.getApiKeysByUser(user).stream()
//                .map(this::mapApiKeyToResponse)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(apiKeys);
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiKeyResponse> generateApiKey(
//            @RequestBody ApiKeyRequest request,
//            Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        ApiKey apiKey = apiKeyService.generateApiKey(user, request.getName());
//        return ResponseEntity.ok(mapApiKeyToResponse(apiKey));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deactivateApiKey(
//            @PathVariable Long id,
//            Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        apiKeyService.deactivateApiKey(id, user);
//        return ResponseEntity.ok(new MessageResponse("API key deactivated successfully"));
//    }
//
//    @GetMapping("/usage")
//    public ResponseEntity<UsageStatsResponse> getUsageStats(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        long totalUsage = apiUsageService.getTotalUsageByUser(user);
//
//        UsageStatsResponse response = new UsageStatsResponse();
//        response.setTotalApiCalls(totalUsage);
//        return ResponseEntity.ok(response);
//    }
//
//    private ApiKeyResponse mapApiKeyToResponse(ApiKey apiKey) {
//        ApiKeyResponse response = new ApiKeyResponse();
//        response.setId(apiKey.getId());
//        response.setKeyValue(apiKey.getKeyValue());
//        response.setName(apiKey.getName());
//        response.setCreatedAt(apiKey.getCreatedAt().toString());
//        response.setLastUsed(apiKey.getLastUsed() != null ? apiKey.getLastUsed().toString() : null);
//        response.setActive(apiKey.isActive());
//        return response;
//    }
//
//    @Data
//    public static class ApiKeyRequest {
//        private String name;
//    }
//
//    @Data
//    public static class ApiKeyResponse {
//        private Long id;
//        private String keyValue;
//        private String name;
//        private String createdAt;
//        private String lastUsed;
//        private boolean active;
//    }
//
//    @Data
//    public static class UsageStatsResponse {
//        private long totalApiCalls;
//    }
//
//    @Data
//    public static class MessageResponse {
//        private final String message;
//    }
//}


package com.example.apimanager.controller;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.ApiUsage;
import com.example.apimanager.model.User;
import com.example.apimanager.service.ApiKeyService;
import com.example.apimanager.service.ApiUsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin
@SecurityRequirement(name = "BearerAuth") // Requires JWT authentication
public class ApiKeyController {
    private final ApiKeyService apiKeyService;
    private final ApiUsageService apiUsageService;

    public ApiKeyController(ApiKeyService apiKeyService, ApiUsageService apiUsageService) {
        this.apiKeyService = apiKeyService;
        this.apiUsageService = apiUsageService;
    }

    @GetMapping
    @Operation(summary = "Get all API keys for the authenticated user", description = "Returns a list of API keys owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "List of API keys retrieved successfully")
    public ResponseEntity<List<ApiKeyResponse>> getMyApiKeys(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<ApiKeyResponse> apiKeys = apiKeyService.getApiKeysByUser(user).stream()
                .map(this::mapApiKeyToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apiKeys);
    }

    @PostMapping
    @Operation(summary = "Generate a new API key", description = "Creates a new API key for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "API key generated successfully")
    public ResponseEntity<ApiKeyResponse> generateApiKey(
            @RequestBody ApiKeyRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ApiKey apiKey = apiKeyService.generateApiKey(user, request.getName());
        return ResponseEntity.ok(mapApiKeyToResponse(apiKey));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate an API key", description = "Deactivates an API key by its ID if owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "API key deactivated successfully")
    public ResponseEntity<?> deactivateApiKey(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        apiKeyService.deactivateApiKey(id, user);
        return ResponseEntity.ok(new MessageResponse("API key deactivated successfully"));
    }

    @GetMapping("/usage")
    @Operation(summary = "Get API usage stats", description = "Returns the total API calls made by the authenticated user's API keys.")
    @ApiResponse(responseCode = "200", description = "Usage stats retrieved successfully")
    public ResponseEntity<UsageStatsResponse> getUsageStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        long totalUsage = apiUsageService.getTotalUsageByUser(user);

        UsageStatsResponse response = new UsageStatsResponse();
        response.setTotalApiCalls(totalUsage);
        return ResponseEntity.ok(response);
    }

    private ApiKeyResponse mapApiKeyToResponse(ApiKey apiKey) {
        ApiKeyResponse response = new ApiKeyResponse();
        response.setId(apiKey.getId());
        response.setKeyValue(apiKey.getKeyValue());
        response.setName(apiKey.getName());
        response.setCreatedAt(apiKey.getCreatedAt().toString());
        response.setLastUsed(apiKey.getLastUsed() != null ? apiKey.getLastUsed().toString() : null);
        response.setActive(apiKey.isActive());
        response.setUsages(apiKey.getUsages().stream().count());
        return response;
    }

    @Data
    public static class ApiKeyRequest {
        private String name;
    }

    @Data
    public static class ApiKeyResponse {
        private Long id;
        private String keyValue;
        private String name;
        private String createdAt;
        private String lastUsed;
        private boolean active;
        private long usages;
    }

    @Data
    public static class UsageStatsResponse {
        private long totalApiCalls;
    }

    @Data
    public static class MessageResponse {
        private final String message;
    }
}