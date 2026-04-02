package com.app.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Integer commentId;
    private String text;
    private LocalDateTime createdAt;
    private String userName;
    private String taskName;
}