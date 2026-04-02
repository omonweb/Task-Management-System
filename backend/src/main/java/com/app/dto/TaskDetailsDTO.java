package com.app.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDetailsDTO {
    private Integer taskId;
    private String taskName;
    private String description; // Added for deep-dive
    private String status;
    private String priority;
    private LocalDate dueDate;

    // Flattened fields
    private String projectName;
    private String userName;

    // Aggregated Category Tags
    private List<String> categories;
}