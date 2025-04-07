package com.example.apimanager.service;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.User;
import com.example.apimanager.repository.ApiKeyRepository;
import com.example.apimanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private static final int MAX_API_KEYS_PER_USER = 10;

    public ApiKeyService(ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    public List<ApiKey> getApiKeysByUser(User user) {
        return apiKeyRepository.findByUser(user);
    }

    @Transactional
    public ApiKey generateApiKey(User user, String name) {
//        int activeKeyCount = apiKeyRepository.countByUserAndActiveTrue(user);
//        if (activeKeyCount >= MAX_API_KEYS_PER_USER) {
//            throw new RuntimeException("Maximum number of API keys reached (10)");
//        }

        ApiKey apiKey = new ApiKey();
        apiKey.setKeyValue(generateUniqueKeyValue());
        apiKey.setName(name);
        apiKey.setCreatedAt(LocalDateTime.now());
        apiKey.setActive(true);
        apiKey.setUser(user);

        return apiKeyRepository.save(apiKey);
    }

    @Transactional
    public void deactivateApiKey(Long keyId, User user) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("API key not found"));

        if (!apiKey.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to deactivate this API key");
        }

        apiKey.setActive(false);
        apiKeyRepository.save(apiKey);
    }

    private String generateUniqueKeyValue() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String keyValue = "ak_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Ensure the key is unique
//        while (apiKeyRepository.findByKeyValue(keyValue).isPresent()) {
//            random.nextBytes(bytes);
//            keyValue = "ak_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
//        }

        return keyValue;
    }
}