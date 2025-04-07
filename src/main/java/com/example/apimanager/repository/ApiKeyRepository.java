package com.example.apimanager.repository;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findByUser(User user);
    int countByUserAndActiveTrue(User user);
    Optional<ApiKey> findByKeyValue(String keyValue);
    Optional<ApiKey> findByKeyValueAndActiveTrue(String keyValue);
}