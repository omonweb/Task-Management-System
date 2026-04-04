package com.app.service;

import com.app.dto.ProjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectClientService {

    private final RestClient restClient;

    public List<ProjectDTO> fetchProjects(String startDate, String endDate) {

        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/projects");
                    if (startDate != null && !startDate.isBlank())
                        uriBuilder.queryParam("startDate", startDate);
                    if (endDate != null && !endDate.isBlank())
                        uriBuilder.queryParam("endDate", endDate);
                    return uriBuilder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProjectDTO>>() {});
    }
}