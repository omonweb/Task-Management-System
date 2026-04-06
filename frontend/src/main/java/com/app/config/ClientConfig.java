package com.app.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class ClientConfig {

    @Value("${backend.api.base-url}")
    private String backendBaseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(backendBaseUrl)
                .requestInterceptor((request, body, execution) -> {
                    // Extract session from the current web request
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest servletRequest = attributes.getRequest();
                        String token = (String) servletRequest.getSession().getAttribute("JWT_TOKEN");

                        // Attach token to the outgoing API call
                        if (token != null && !token.isEmpty()) {
                            request.getHeaders().setBearerAuth(token);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}