package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long inProgressTasks;
}