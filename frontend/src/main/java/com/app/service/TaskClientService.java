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

    public PaginatedResponse<TaskDTO> fetchTasks(int page, int size) {
        return restClient.get()
                .uri("/tasks?page={page}&size={size}", page, size)
                .retrieve()
                .body(new ParameterizedTypeReference<PaginatedResponse<TaskDTO>>() {});
    }
}