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
    private String projectName;
    private String userName;
}