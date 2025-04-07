package com.example.apimanager.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class ApiUsageController {

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "API data accessed successfully");
        data.put("timestamp", LocalDateTime.now().toString());
        data.put("status", "success");

        return ResponseEntity.ok(data);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getStatus() {
        StatusResponse response = new StatusResponse();
        response.setStatus("online");
        response.setTimestamp(LocalDateTime.now().toString());
        response.setVersion("1.0.0");

        return ResponseEntity.ok(response);
    }

    @Data
    public static class StatusResponse {
        private String status;
        private String timestamp;
        private String version;
    }
}
