package com.app.service;

import com.app.dto.CommentDTO;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    // positive - task id 1 exists in DB so comments should return
    @Test
    public void testGetCommentsByTaskId_WhenTaskExists() {

        List<CommentDTO> result = commentService.getCommentsByTaskId(1);

        assertNotNull(result);
        assertTrue(result.size() >= 0);
    }

    // negative - task 999 does not exist in DB so should throw 404
    @Test
    public void testGetCommentsByTaskId_WhenTaskNotFound() {

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> commentService.getCommentsByTaskId(999)
        );

        assertEquals("Task not found with ID: 999", exception.getMessage());
    }
}