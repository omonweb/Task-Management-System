package com.app.controller;

import com.app.dto.UserDTO;
import com.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String username
    ) {
        return userService.getAllUsers(role, username);
    }
}