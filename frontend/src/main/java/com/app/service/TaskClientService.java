package com.app.service;

import com.app.dto.PaginatedResponse;
import com.app.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TaskClientService {

    private final RestClient restClient;

    public PaginatedResponse<TaskDTO> fetchTasks(String priority, String status, int page, int size) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/tasks");
                    // Only append the query parameter if the user selected a filter
                    if (priority != null && !priority.isBlank()) uriBuilder.queryParam("priority", priority);
                    if (status != null && !status.isBlank()) uriBuilder.queryParam("status", status);
                    uriBuilder.queryParam("page", page);
                    uriBuilder.queryParam("size", size);
                    return uriBuilder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<PaginatedResponse<TaskDTO>>() {});
    }
}