package com.app.service;

import com.app.dto.UserDTO;
import com.app.entity.User;
import java.util.List;



public interface UserService {
    List<UserDTO> getAllUsers(String role, String username);
}