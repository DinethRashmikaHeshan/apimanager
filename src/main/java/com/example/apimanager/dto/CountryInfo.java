package com.example.apimanager.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CountryInfo {
    private String name;
    private Map<String, String> currencies; // e.g., {"USD": "United States Dollar"}
    private String capital;
    private Map<String, String> languages; // e.g., {"en": "English"}
    private String flagUrl; // URL to the flag image
}