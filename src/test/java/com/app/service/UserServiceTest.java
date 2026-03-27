package com.app.service;

import com.app.entity.User;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testGetAllUsers() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        UserServiceImpl service = new UserServiceImpl(repo);

        User user = new User();
        user.setUserId(1);
        user.setUsername("lakshay");
        user.setEmail("lakshay@email.com");
        user.setFullName("Lakshay");

        Mockito.when(repo.findAll()).thenReturn(List.of(user));

        var result = service.getAllUsers(null, null);

        assertEquals(1, result.size());
        assertEquals("lakshay", result.get(0).getUsername());
    }
}
