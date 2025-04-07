package com.example.apimanager.service;

import com.example.apimanager.dto.CountryInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final RestTemplate restTemplate;

    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CountryInfo> getAllCountries() {
        String url = "https://restcountries.com/v3.1/all";
        Object[] countries = restTemplate.getForObject(url, Object[].class);

        if (countries == null) {
            throw new RuntimeException("Failed to fetch country data");
        }

        return Arrays.stream(countries)
                .map(this::mapToCountryInfo)
                .collect(Collectors.toList());
    }

    public CountryInfo getCountryByName(String name) {
        String url = "https://restcountries.com/v3.1/name/" + name + "?fullText=true";
        Object[] countries = restTemplate.getForObject(url, Object[].class);

        if (countries == null || countries.length == 0) {
            throw new RuntimeException("Country not found: " + name);
        }

        // Return the first match (assuming exact name match with fullText=true)
        return mapToCountryInfo(countries[0]);
    }

    @SuppressWarnings("unchecked")
    private CountryInfo mapToCountryInfo(Object countryObj) {
        Map<String, Object> country = (Map<String, Object>) countryObj;
        CountryInfo info = new CountryInfo();

        // Country name
        Map<String, String> nameMap = (Map<String, String>) country.get("name");
        info.setName(nameMap != null ? nameMap.get("common") : "Unknown");

        // Currency information
        Map<String, Map<String, String>> currenciesMap = (Map<String, Map<String, String>>) country.get("currencies");
        Map<String, String> currencies = new HashMap<>();
        if (currenciesMap != null) {
            currenciesMap.forEach((code, details) -> currencies.put(code, details.get("name")));
        }
        info.setCurrencies(currencies);

        // Capital city
        List<String> capitalList = (List<String>) country.get("capital");
        info.setCapital(capitalList != null && !capitalList.isEmpty() ? capitalList.get(0) : "N/A");

        // Spoken languages
        Map<String, String> languages = (Map<String, String>) country.get("languages");
        info.setLanguages(languages != null ? languages : new HashMap<>());

        // National flag (URL)
        Map<String, String> flags = (Map<String, String>) country.get("flags");
        info.setFlagUrl(flags != null ? flags.get("png") : "N/A");

        return info;
    }
}