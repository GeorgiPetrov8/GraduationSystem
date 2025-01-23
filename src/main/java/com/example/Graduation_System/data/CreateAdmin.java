package com.example.Graduation_System.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.services.UserService;

@Component
public class CreateAdmin implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        // Check if the database is empty or if the admin user already exists
        if (userService.findAll().isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("adminpassword");
            admin.setRoles(UserRole.ADMIN);  // Assign role ADMIN
            userService.createUser(admin);  // Create the admin user in the database
        }
    }
}

