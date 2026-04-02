package com.app.service;

import com.app.dto.CommentDTO;
import com.app.entity.Comment;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CommentRepository;
import com.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<CommentDTO> getCommentsByTaskId(Integer taskId) {

        // Check if task exists — throws 404 if not
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }

        // Sort newest first
        Sort sortByCreatedAtDesc = Sort.by(Sort.Direction.DESC, "createdAt");

        // Fetch comments for this task
        List<Comment> comments = commentRepository.findByTask_TaskId(taskId, sortByCreatedAtDesc);

        // Convert to DTO
        List<CommentDTO> result = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO dto = new CommentDTO();

            dto.setCommentId(comment.getCommentId());
            dto.setText(comment.getText());
            dto.setCreatedAt(comment.getCreatedAt());

            if (comment.getUser() != null) {
                dto.setUserName(comment.getUser().getUsername());
            } else {
                dto.setUserName("Unknown User");
            }

            if (comment.getTask() != null) {
                dto.setTaskName(comment.getTask().getTaskName());
            } else {
                dto.setTaskName("Unknown Task");
            }

            result.add(dto);
        }

        return result;
    }
}