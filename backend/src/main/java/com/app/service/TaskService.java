package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<TaskDTO> getTasks(String priority, String status, int page, int size);

    // method signature to fetch specific task's details.
    TaskDetailsDTO getTaskById(Integer id);
}