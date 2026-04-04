package com.app.controller;

import com.app.dto.UserDTO;
import com.app.service.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserWebController {

    private final UserClientService userClientService;

    @GetMapping("/users")
    public String viewUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String username,
            Model model) {

        try {
            List<UserDTO> users = userClientService.fetchUsers(role, username);
            model.addAttribute("users", users);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("users", null);
            model.addAttribute("error", "No users found or an error occurred.");
        }

        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedUsername", username);

        return "users";
    }
}
