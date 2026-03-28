package com.app.controller;

import com.app.dto.CommentDTO;
import com.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(
            @PathVariable Integer id) {

        List<CommentDTO> comments = commentService.getCommentsByTaskId(id);
        return ResponseEntity.ok(comments);
    }
}
