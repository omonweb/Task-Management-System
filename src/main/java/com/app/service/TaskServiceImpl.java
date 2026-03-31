package com.app.service;

import com.app.dto.TaskDTO;
import com.app.entity.Task;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<TaskDTO> getTasks(String priority, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage;

        boolean hasPriority = (priority != null && !priority.trim().isEmpty());
        boolean hasStatus = (status != null && !status.trim().isEmpty());

        // Routing logic based on provided query parameters using if-else to allow for better readability and also improve performance.
        if (hasPriority && hasStatus) {
            taskPage = taskRepository.findByPriorityIgnoreCaseAndStatusIgnoreCase(priority, status, pageable);
        } else if (hasPriority) {
            taskPage = taskRepository.findByPriorityIgnoreCase(priority, pageable);
        } else if (hasStatus) {
            taskPage = taskRepository.findByStatusIgnoreCase(status, pageable);
        } else {
            // Neither provided -> Fetch all tasks
            taskPage = taskRepository.findAll(pageable);
        }

        // Exception Handling: Page Out of Bounds
        if (page > 0 && page >= taskPage.getTotalPages() && taskPage.getTotalElements() > 0) {
            throw new ResourceNotFoundException("Page index out of bounds. Total pages available: " + taskPage.getTotalPages());
        }

        // Map Entity to DTO using modelMapper!
        return taskPage.map(task -> {
            TaskDTO dto = modelMapper.map(task, TaskDTO.class);

            if (task.getProject() != null) {
                dto.setProjectName(task.getProject().getProjectName());
            }
            if (task.getUser() != null) {
                dto.setUserName(task.getUser().getUsername());
            }

            return dto;
        });
    }
}