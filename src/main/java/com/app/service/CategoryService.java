package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.dto.TaskDTO;
import java.util.List;

public interface CategoryService {

    CategoryDTO getCategoryWithTaskCounts(Integer categoryId);

    List<TaskDTO> getTasksByCategoryName(String categoryName);
}