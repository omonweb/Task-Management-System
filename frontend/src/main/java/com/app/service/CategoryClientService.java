package com.app.service;

import com.app.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryClientService {

    private final RestClient restClient;

    public List<TaskDTO> fetchTasksByCategoryName(String name) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/categories/tasks");
                    if (name != null && !name.isBlank())
                        uriBuilder.queryParam("name", name);
                    return uriBuilder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<TaskDTO>>() {});
    }
}