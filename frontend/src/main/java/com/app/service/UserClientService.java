package com.app.service;

import com.app.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClientService {

    private final RestClient restClient;

    public List<UserDTO> fetchUsers(String role, String username) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/users");
                    if (role != null && !role.isBlank()) {
                        uriBuilder.queryParam("role", role);
                    }
                    if (username != null && !username.isBlank()) {
                        uriBuilder.queryParam("username", username);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserDTO>>() {});
    }
}
