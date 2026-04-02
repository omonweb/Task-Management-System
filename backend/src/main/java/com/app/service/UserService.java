package com.app.service;

import com.app.dto.UserDTO;
import com.app.dto.UserDetailsDTO;
import com.app.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    List<UserDTO> getUsersFiltered(String role, String username);
    UserDetailsDTO getUserDetails(Integer userId);
}