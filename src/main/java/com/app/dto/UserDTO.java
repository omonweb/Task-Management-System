package com.app.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
}