package com.app.service;

import com.app.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationClientService {

    private final RestClient restClient;

    public List<NotificationDTO> fetchNotificationsByUserId(Integer userId) {
        try {
            return restClient.get()
                    .uri("/notifications?userId={userId}", userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new IllegalArgumentException(
                                "User with ID " + userId + " does not exist.");
                    })
                    .body(new ParameterizedTypeReference<List<NotificationDTO>>() {});
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to fetch notifications: " + e.getMessage());
        }
    }
}
