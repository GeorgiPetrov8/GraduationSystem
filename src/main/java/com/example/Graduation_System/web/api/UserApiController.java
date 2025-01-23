package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.LoginRequest;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.services.TeacherService;
import com.example.Graduation_System.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody LoginRequest loginRequest) {
        String roles = userService.findRolesByUsername(loginRequest.username()).toString();

        String token = userService.loginUser(loginRequest.username(), loginRequest.password(), roles);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("roles", roles);

        return response;
    }

}
