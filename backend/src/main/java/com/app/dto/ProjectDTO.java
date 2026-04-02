package com.app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Integer projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerName;
}