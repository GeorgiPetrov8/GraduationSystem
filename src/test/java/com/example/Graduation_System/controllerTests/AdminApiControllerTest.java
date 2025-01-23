package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.Department;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.services.*;
import com.example.Graduation_System.web.api.AdminApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private AdminApiController adminApiController;

    private User mockUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminApiController).build();
        mockUser = new User();
        mockUser.setUsername("admin");
        mockUser.setPassword("password123");
        mockUser.setRoles(UserRole.ADMIN);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateUserSuccess() throws Exception {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password123");
        newUser.setRoles(UserRole.STUDENT);

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/admin/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password123\", \"roles\": \"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("oldpassword");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(existingUser);

        mockMvc.perform(put("/api/admin/users/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\", \"password\": \"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("existingUser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/admin/users/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllTeachers() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setName("Teacher 1");
        Teacher teacher2 = new Teacher();
        teacher2.setName("Teacher 2");

        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(teacher1, teacher2));

        mockMvc.perform(get("/api/admin/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Teacher 1"))
                .andExpect(jsonPath("$[1].name").value("Teacher 2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDepartment() throws Exception {
        Department department = new Department();
        department.setName("Computer Science");

        when(departmentService.createDepartment(any(Department.class))).thenReturn(department);

        mockMvc.perform(post("/api/admin/departments/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Computer Science\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllDepartments() throws Exception {
        Department department1 = new Department();
        department1.setName("Computer Science");
        Department department2 = new Department();
        department2.setName("Mathematics");

        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(department1, department2));

        mockMvc.perform(get("/api/admin/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Computer Science"))
                .andExpect(jsonPath("$[1].name").value("Mathematics"));
    }
}
