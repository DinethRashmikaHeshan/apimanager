//package com.example.apimanager.controller;
//
//import com.example.apimanager.dto.CountryInfo;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.apimanager.service.CountryService;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/external")
//public class ApiUsageController {
//
//    private final CountryService countryService;
//
//    public ApiUsageController(CountryService countryService) {
//        this.countryService = countryService;
//    }
//
//    @GetMapping("/data")
//    public ResponseEntity<Map<String, Object>> getData() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("message", "API data accessed successfully");
//        data.put("timestamp", LocalDateTime.now().toString());
//        data.put("status", "success");
//
//        return ResponseEntity.ok(data);
//    }
//
//    @GetMapping("/status")
//    public ResponseEntity<StatusResponse> getStatus() {
//        StatusResponse response = new StatusResponse();
//        response.setStatus("online");
//        response.setTimestamp(LocalDateTime.now().toString());
//        response.setVersion("1.0.0");
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/countries")
//    public ResponseEntity<List<CountryInfo>> getCountries() {
//        List<CountryInfo> countries = countryService.getAllCountries();
//        return ResponseEntity.ok(countries);
//    }
//
//    @GetMapping("/countries/{name}")
//    public ResponseEntity<CountryInfo> getCountryByName(@PathVariable String name) {
//        CountryInfo country = countryService.getCountryByName(name);
//        return ResponseEntity.ok(country);
//    }
//
//    @Data
//    public static class StatusResponse {
//        private String status;
//        private String timestamp;
//        private String version;
//    }
//}

package com.example.apimanager.controller;

import com.example.apimanager.dto.CountryInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apimanager.service.CountryService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
@SecurityRequirement(name = "ApiKeyAuth") // Requires API key authentication
@SecurityRequirement(name = "BearerAuth")
@CrossOrigin
public class ApiUsageController {

    private final CountryService countryService;

    public ApiUsageController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/data")
    @Operation(summary = "Get sample external API data", description = "Returns a simple success message with a timestamp.")
    @ApiResponse(responseCode = "200", description = "Data retrieved successfully")
    public ResponseEntity<Map<String, Object>> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "API data accessed successfully");
        data.put("timestamp", LocalDateTime.now().toString());
        data.put("status", "success");

        return ResponseEntity.ok(data);
    }

    @GetMapping("/status")
    @Operation(summary = "Get API status", description = "Returns the current status of the external API.")
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    public ResponseEntity<StatusResponse> getStatus() {
        StatusResponse response = new StatusResponse();
        response.setStatus("online");
        response.setTimestamp(LocalDateTime.now().toString());
        response.setVersion("1.0.0");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/countries")
    @Operation(summary = "Get all countries", description = "Fetches a list of all countries with essential information.")
    @ApiResponse(responseCode = "200", description = "Countries retrieved successfully")
    public ResponseEntity<List<CountryInfo>> getCountries() {
        List<CountryInfo> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/countries/{name}")
    @Operation(summary = "Get country by name", description = "Fetches details of a specific country by its name.")
    @ApiResponse(responseCode = "200", description = "Country retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Country not found")
    public ResponseEntity<CountryInfo> getCountryByName(@PathVariable String name) {
        CountryInfo country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }

    @Data
    public static class StatusResponse {
        private String status;
        private String timestamp;
        private String version;
    }
}