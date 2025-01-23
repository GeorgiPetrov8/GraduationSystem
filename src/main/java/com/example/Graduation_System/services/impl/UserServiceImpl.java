package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.cfg.JwtUtil;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.UserRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ValidationException("Username cannot be null or empty.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidationException("Password cannot be null or empty.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRoles() == null) {
            user.setRoles(UserRole.STUDENT);
        } else {
            user.setRoles(user.getRoles());
        }
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().name())
                .build();
    }

   @Override
    public String findRolesByUsername(String username) {
        UserDetails userDetails = loadUserByUsername(username);

        if (userDetails != null && !userDetails.getAuthorities().isEmpty()) {
            return userDetails.getAuthorities().iterator().next().getAuthority();
        }

        return null;
    }

    private boolean authenticateUser(String username, String enteredPassword) {
        try {
            UserDetails userDetails = loadUserByUsername(username);

            return passwordEncoder.matches(enteredPassword, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @Override
    public String loginUser(String username, String password, String roles) {

        if (authenticateUser(username, password)) {
            return jwtUtil.generateTokenForUser(username, roles); // Return JWT if successful
        } else {
            throw new ValidationException("Invalid credentials.");
        }
    }


    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            user.setRoles(updatedUser.getRoles());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
    }

}
