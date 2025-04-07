package com.example.apimanager.security;

import com.example.apimanager.model.ApiKey;
import com.example.apimanager.model.ApiUsage;
import com.example.apimanager.repository.ApiKeyRepository;
import com.example.apimanager.service.ApiUsageService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private final ApiKeyRepository apiKeyRepository;
    private final ApiUsageService apiUsageService;

    public ApiKeyAuthenticationFilter(ApiKeyRepository apiKeyRepository, ApiUsageService apiUsageService) {
        this.apiKeyRepository = apiKeyRepository;
        this.apiUsageService = apiUsageService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Only check for API key on the specific API endpoint
        if (request.getRequestURI().startsWith("/api/external")) {
            final String apiKeyHeader = request.getHeader("X-API-KEY");

            if (apiKeyHeader == null || apiKeyHeader.isEmpty()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("API key is required");
                return;
            }

            Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByKeyValueAndActiveTrue(apiKeyHeader);

            if (apiKeyOpt.isEmpty()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid API key");
                return;
            }

            ApiKey apiKey = apiKeyOpt.get();
            apiKey.setLastUsed(LocalDateTime.now());
            apiKeyRepository.save(apiKey);

            // Create an ApiUsage entry
            ApiUsage apiUsage = new ApiUsage();
            apiUsage.setApiKey(apiKey);
            apiUsage.setUser(apiKey.getUser());
            apiUsage.setTimestamp(LocalDateTime.now());
            apiUsage.setEndpoint(request.getRequestURI());
            apiUsageService.saveApiUsage(apiUsage);

            // Authenticate the user associated with the API key
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    apiKey.getUser(), null, apiKey.getUser().getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/external");
    }
}