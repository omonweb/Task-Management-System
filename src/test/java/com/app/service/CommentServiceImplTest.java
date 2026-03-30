package com.app.service;

import com.app.dto.CommentDTO;
import com.app.entity.Comment;
import com.app.entity.Task;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CommentRepository;
import com.app.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void getCommentsByTaskId_success() {

        Task task = new Task();
        task.setTaskId(1);
        task.setTaskName("Login Feature");

        User user = new User();
        user.setUserId(1);
        user.setUsername("john");

        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setText("Good work!");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user);

        when(taskRepository.existsById(1)).thenReturn(true);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        when(commentRepository.findByTask_TaskId(1, sort))
                .thenReturn(List.of(comment));

        List<CommentDTO> result = commentService.getCommentsByTaskId(1);

        assertEquals(1, result.size());
        assertEquals("Good work!", result.get(0).getText());
        assertEquals("john", result.get(0).getUserName());
        assertEquals("Login Feature", result.get(0).getTaskName());
    }

    @Test
    void getCommentsByTaskId_taskNotFound() {

        when(taskRepository.existsById(999)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentsByTaskId(999);
        });
    }
}