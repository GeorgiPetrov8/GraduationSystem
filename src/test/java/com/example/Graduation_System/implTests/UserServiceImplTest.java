package com.example.Graduation_System.implTests;

import com.example.Graduation_System.cfg.JwtUtil;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.UserRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setRoles(UserRole.STUDENT);
    }

    @Test
    void testCreateUser() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getRoles(), result.getRoles());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserWithExistingUsername() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(user));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals("ROLE_STUDENT", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsernameNotFound() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.loadUserByUsername(user.getUsername()));
        assertEquals("User not found with username: john_doe", exception.getMessage());
    }

    @Test
    void testLoginUserSuccess() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenForUser(user.getUsername(), user.getRoles().name())).thenReturn("mockJwtToken");

        String token = userService.loginUser(user.getUsername(), user.getPassword(), user.getRoles().name());

        assertNotNull(token);
        assertEquals("mockJwtToken", token);
    }

    @Test
    void testLoginUserInvalidCredentials() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.loginUser(user.getUsername(), user.getPassword(), user.getRoles().name()));
        assertEquals("Invalid credentials.", exception.getMessage());
    }

    @Test
    void testUpdateUser() {

        User updatedUser = new User();
        updatedUser.setUsername("new_username");
        updatedUser.setPassword("new_password123");
        updatedUser.setRoles(UserRole.TEACHER);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user.getId(), updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getRoles(), result.getRoles());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {

        User updatedUser = new User();
        updatedUser.setUsername("new_username");

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(user.getId(), updatedUser));
        assertEquals("User with ID " + user.getId() + " not found.", exception.getMessage());
    }

    @Test
    void testDeleteUser() {

        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testDeleteUserNotFound() {

        when(userRepository.existsById(user.getId())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(user.getId()));
        assertEquals("User with ID " + user.getId() + " not found.", exception.getMessage());
    }
}
