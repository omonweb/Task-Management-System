package com.app.service;

import com.app.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationClientService {

    private final RestClient restClient;

    public List<NotificationDTO> fetchNotificationsByUserId(Integer userId) {
        return restClient.get()
                .uri("/notifications?userId={userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<NotificationDTO>>() {});
    }
}
