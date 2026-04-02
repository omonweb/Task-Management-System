package com.app.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String roleName;
}