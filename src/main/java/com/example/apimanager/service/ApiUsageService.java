package com.example.apimanager.service;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.ApiUsage;
import com.example.apimanager.model.User;
import com.example.apimanager.repository.ApiUsageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUsageService {
    private final ApiUsageRepository apiUsageRepository;

    public ApiUsageService(ApiUsageRepository apiUsageRepository) {
        this.apiUsageRepository = apiUsageRepository;
    }

    public ApiUsage saveApiUsage(ApiUsage apiUsage) {
        return apiUsageRepository.save(apiUsage);
    }

    public List<ApiUsage> getApiUsageByUser(User user) {
        return apiUsageRepository.findByUser(user);
    }

    public List<ApiUsage> getApiUsageByApiKey(ApiKey apiKey) {
        return apiUsageRepository.findByApiKey(apiKey);
    }

    public long getTotalUsageByUser(User user) {
        return apiUsageRepository.countByUser(user);
    }
}