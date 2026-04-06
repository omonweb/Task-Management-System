package com.app.security;

import com.app.entity.User;
import com.app.entity.UserRoles;
import com.app.repository.UserRepository;
import com.app.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Find the user in your database
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Fetch their roles
        List<UserRoles> roles = userRolesRepository.findByUserUserId(user.getUserId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (UserRoles role : roles) {
            String safeRoleName = role.getUserRole().getRoleName().toUpperCase().replace(" ", "_");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + safeRoleName));
        }

        // 3. Hand the credentials to Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}