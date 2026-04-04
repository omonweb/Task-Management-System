package com.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number; // current page number
    private int size;
    private boolean first;
    private boolean last;
}