package com.app.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginWebController {

    private final RestClient restClient;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session) {
        try {
            // Ask backend for a token
            Map response = restClient.post()
                    .uri("/auth/login")
                    .body(Map.of("username", username, "password", password))
                    .retrieve()
                    .body(Map.class);

            // Store token in frontend session
            if (response != null && response.containsKey("token")) {
                session.setAttribute("JWT_TOKEN", response.get("token"));
                return "redirect:/tasks"; // Go to dashboard on success
            }
            return "redirect:/login?error=true";

        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroy the token
        return "redirect:/login?logout=true";
    }
}