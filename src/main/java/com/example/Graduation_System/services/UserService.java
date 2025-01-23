package com.example.Graduation_System.services;

import com.example.Graduation_System.data.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User createUser(User user);

    String findRolesByUsername(String username);

    String loginUser(String username, String password, String roles);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    UserDetails loadUserByUsername(String username);
}
