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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    /*
    @Override
    public List<TaskDTO> getCompletedTasks() {
        // Ask for the data
        List<Task> rawtask = taskRepository.findByStatusIgnoreCase("Completed");

        //convert entity to dto
        List<TaskDTO> tasks = new ArrayList<>();
        for(Task task : rawtask) {
            TaskDTO dto = new TaskDTO();
            dto.setTaskId(task.getTaskId());
            dto.setTaskName(task.getTaskName());
            dto.setStatus(task.getStatus());
            tasks.add(dto);
        }

        return tasks;
    }

    @Override
    public List<TaskDTO> getTaskbyUser(Integer id) {
        //get raw data
        List<Task> tasks = taskRepository.findByUser_UserId(id);

        //map entity to dto

//          List<TaskDTO> result = new ArrayList<>();
//        for(Task task : tasks) {
//            TaskDTO dto = new TaskDTO();
//            dto.setStatus(task.getStatus());
//            dto.setUserName(task.getUser().getUsername());
//            dto.setTaskName(task.getTaskName());
//            dto.setTaskId(task.getTaskId());
//            result.add(dto);
//        }

        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .toList();

        // return result;
    }

    @Override
    public List<TaskDTO> getTasksbyKeyword(String taskName) {
        List<Task> tasks = taskRepository.findByTaskNameContainsIgnoreCase(taskName);

        //map to dto

        return tasks.stream()
                .map(task -> modelMapper.map(task,TaskDTO.class))
                .toList();
    }

    @Override
    public List<Object[]> getTaskStatusCounts() {

        // 1. Get raw data from the pantry
        List<Object[]> rawData = taskRepository.countTasksGroupedByStatus();

        return rawData;
        /*
        // 2. Prepare our clean Map (Dictionary)
        Map<String, Long> statusCounts = new HashMap<>();

        // 3. Loop through the raw rows and put them in the map
        for (Object[] row : rawData) {
            String status = (String) row[0]; // The first column is the status
            Long count = (Long) row[1];      // The second column is the count

            statusCounts.put(status, count);
        }

        return statusCounts;
    }
    @Override
    public List<Object[]> getTaskPriorityCounts() {
        List<Object[]> raws = taskRepository.countTasksGroupedByPriority();

        return raws;
    }

    @Override
    public Map<String, Long> getTaskProjectCounts(){
        List<Object[]> rawData = taskRepository.countTasksGroupedByProjects();

        HashMap<String,Long> counts = new HashMap<>();

        for(Object[] row : rawData) {
            counts.put((String)row[0],(Long)row[1]);
        }
        return counts;
    }
*/
}