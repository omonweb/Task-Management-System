package com.app.service;

import com.app.dto.UserDTO;
import com.app.entity.User;
import com.app.entity.UserRoles;
import com.app.repository.UserRepository;
import com.app.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> getUsersFiltered(String role, String username) {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user ->
                        username == null ||
                                user.getUsername().toLowerCase().contains(username.toLowerCase())
                )
                .map(user -> {

                    UserDTO dto = new UserDTO();

                    dto.setUserId(Long.valueOf(user.getUserId()));
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(user.getFullName());

                    List<UserRoles> roles =
                            userRolesRepository.findByUserUserId(user.getUserId());

                    if (!roles.isEmpty()) {
                        String roleName = roles.get(0).getUserRole().getRoleName();

                        if (role == null || roleName.equalsIgnoreCase(role)) {
                            dto.setRoleName(roleName);
                        } else {
                            return null; // filter out
                        }
                    }

                    return dto;
                })
                .filter(dto -> dto != null)
                .toList();
    }
}