package com.app.controller;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import com.app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // the path is just /api/tasks now
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> getTasks(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TaskDTO> tasks = taskService.getTasks(priority, status, page, size);
        return ResponseEntity.ok(tasks);
    }

    // get all details of a specific task.
    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsDTO> getTaskById(@PathVariable Integer id) {
        TaskDetailsDTO taskDetails = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDetails);
    }
}