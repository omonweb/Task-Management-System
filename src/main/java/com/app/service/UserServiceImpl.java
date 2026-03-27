package com.app.service;

import com.app.dto.UserDTO;
import com.app.entity.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers(String role, String username) {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> {

                    boolean matchesUsername = (username == null || username.isEmpty())
                            || user.getUsername().toLowerCase().contains(username.toLowerCase());

                    boolean matchesRole = true;

                    if (role != null && !role.isEmpty()) {
                        matchesRole = user.getUserRoles().stream()
                                .anyMatch(ur -> ur.getUserRole().getRoleName().equalsIgnoreCase(role));
                    }

                    return matchesUsername && matchesRole;
                })
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setUserId(user.getUserId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .toList();
    }
}