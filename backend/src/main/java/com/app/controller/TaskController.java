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
    /*

    @GetMapping("/complete")
    public ResponseEntity<List<TaskDTO>> getCompletedTasks() {
        List<TaskDTO> task = taskService.getCompletedTasks();
        return ResponseEntity.ok(task);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksbyUser(
            @PathVariable Integer id) {
        List<TaskDTO> tasks = taskService.getTaskbyUser(id);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> getTasksbyKeyword(
            @RequestParam String taskName) {
        List<TaskDTO> tasks = taskService.getTasksbyKeyword(taskName);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/counts")
    public ResponseEntity<List<Object[]>> getCounts() {
        List<Object[]> result = taskService.getTaskStatusCounts() ;

        return ResponseEntity.ok(result);
    }

    @GetMapping("/priority/counts")
    public ResponseEntity<List<Object[]>> getCountsByPriority() {
        List<Object[]> result = taskService.getTaskPriorityCounts();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/project/counts")
    public ResponseEntity<Map<String,Long>> getCountsByProjects() {
        Map<String , Long> result = taskService.getTaskProjectCounts();

        return ResponseEntity.ok(result);
    }*/
}

/// api/tasks/user/5