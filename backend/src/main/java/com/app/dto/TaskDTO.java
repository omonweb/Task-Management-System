package com.app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskDTO {
    private Integer taskId;
    private String taskName;
    private String status;
    private String priority;
    private LocalDate dueDate;

    // Flattened fields from Joins
    private String projectName;
    private String userName;
}