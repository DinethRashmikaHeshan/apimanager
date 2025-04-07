package com.example.apimanager.repository;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.ApiUsage;
import com.example.apimanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
    List<ApiUsage> findByUser(User user);
    List<ApiUsage> findByApiKey(ApiKey apiKey);
    long countByUser(User user);
}
