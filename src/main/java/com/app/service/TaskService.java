package com.app.service;

import com.app.dto.TaskDTO;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<TaskDTO> getTasks(String priority, String status, int page, int size);
}