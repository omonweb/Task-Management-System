package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import com.app.entity.Task;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (hasPriority && hasStatus) {
            taskPage = taskRepository.findByPriorityIgnoreCaseAndStatusIgnoreCase(priority, status, pageable);
        } else if (hasPriority) {
            taskPage = taskRepository.findByPriorityIgnoreCase(priority, pageable);
        } else if (hasStatus) {
            taskPage = taskRepository.findByStatusIgnoreCase(status, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        if (page > 0 && page >= taskPage.getTotalPages() && taskPage.getTotalElements() > 0) {
            throw new ResourceNotFoundException("Page index out of bounds. Total pages available: " + taskPage.getTotalPages());
        }

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

    @Override
    public TaskDetailsDTO getTaskById(Integer id) {
        // 1. Fetch Task or throw 404
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        // 2. Map core entity to DTO
        TaskDetailsDTO dto = modelMapper.map(task, TaskDetailsDTO.class);

        // 3. Map flattened relational fields (Project & User)
        if (task.getProject() != null) {
            dto.setProjectName(task.getProject().getProjectName());
        }
        if (task.getUser() != null) {
            dto.setUserName(task.getUser().getUsername());
        }

        // 4. Extract and map the Category names from the TaskCategory join table
        if (task.getTaskCategories() != null) {
            List<String> categoryNames = task.getTaskCategories().stream()
                    .map(tc -> tc.getCategory().getCategoryName())
                    .toList();
            dto.setCategories(categoryNames);
        }

        return dto;
    }
}