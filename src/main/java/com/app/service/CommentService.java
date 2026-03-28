package com.app.service;

import com.app.dto.CommentDTO;
import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentsByTaskId(Integer taskId);
}