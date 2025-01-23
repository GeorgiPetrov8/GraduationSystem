package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.LoginRequest;
import com.example.Graduation_System.services.UserService;
import com.example.Graduation_System.web.api.StudentApiController;
import com.example.Graduation_System.web.api.UserApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApiController userApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userApiController).build();
    }

    @Test
    void testLoginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");

        String mockToken = "mockToken";
        String mockRoles = "ROLE_STUDENT";
        when(userService.findRolesByUsername("testuser")).thenReturn(mockRoles);
        when(userService.loginUser("testuser", "password123", mockRoles)).thenReturn(mockToken);

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(mockToken))
                .andExpect(jsonPath("$.roles").value(mockRoles));
    }
}
