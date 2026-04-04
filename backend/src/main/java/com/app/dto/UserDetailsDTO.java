package com.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailsDTO {

    private Integer userId;
    private String username;
    private String email;
    private String fullName;

    private List<String> projectNames;
    private List<String> taskNames;
}
