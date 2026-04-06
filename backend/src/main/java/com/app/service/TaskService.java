package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Page<TaskDTO> getTasks(String priority, String status, int page, int size);

    // method signature to fetch specific task's details.
    TaskDetailsDTO getTaskById(Integer id);

    /*
    List<TaskDTO> getCompletedTasks();

    public List<TaskDTO> getTaskbyUser(Integer id);

    public List<TaskDTO> getTasksbyKeyword(String taskName);
    public List<Object[]> getTaskStatusCounts();
    public List<Object[]> getTaskPriorityCounts();
    public Map<String , Long> getTaskProjectCounts();*/
}