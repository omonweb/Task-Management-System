package com.app.service;

import com.app.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO getCategoryWithTaskCounts(Integer categoryId);
}