package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.*;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.enums.TeacherPosition;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.services.*;
import com.example.Graduation_System.web.api.StudentApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class StudentApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiplomaAssignmentService diplomaAssignmentService;

    @Mock
    private DiplomaThesisService diplomaThesisService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentApiController studentApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentApiController).build();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testViewApprovedAssignments() throws Exception {

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setStatus(DiplomaStatus.APPROVED);
        when(diplomaAssignmentService.getApprovedAssignments()).thenReturn(List.of(assignment));

        mockMvc.perform(get("/api/students/assignments/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(DiplomaStatus.APPROVED.toString()));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testSubmitThesis() throws Exception {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(UserRole.STUDENT);
        userService.createUser(user);

        User user1 = new User();
        user1.setUsername("testuser1");
        user1.setPassword("password123");
        user1.setRoles(UserRole.TEACHER);
        userService.createUser(user1);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentService.saveStudent(student);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherService.saveTeacher(teacher);

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setTitle("test");
        assignment.setGoal("test");
        assignment.setTasks("test");
        assignment.setTechnologies("test");
        assignment.setFacultyNumber(student.getFacultyNumber());
        assignment.setStatus(DiplomaStatus.APPROVED);
        assignment.setStudent(student);
        assignment.setSupervisor(teacher);
        assignment.setSupervisorName(teacher.getName());
        diplomaAssignmentService.saveAssignment(assignment);

        DiplomaThesis thesis = new DiplomaThesis();
        thesis.setTitle("Thesis Title");
        thesis.setText("test");
        thesis.setAssignment(assignment);
        thesis.setFacultyNumber(student.getFacultyNumber());
        diplomaThesisService.saveThesis(thesis);

        when(diplomaThesisService.saveThesis(any(DiplomaThesis.class))).thenReturn(thesis);

        mockMvc.perform(post("/api/students/thesis")
                        .contentType("application/json")
                        .content("{\"facultyNumber\": \"12345678\", \"title\": \"Thesis Title\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(jsonPath("$.facultyNumber").value("12345678"))
                .andExpect(jsonPath("$.title").value("Thesis Title"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetGraduatedStudents() throws Exception {

        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 1);
        Student student = new Student();
        student.setGraduated(LocalDate.now());
        when(studentService.getGraduatedStudentsInPeriod(startDate, endDate)).thenReturn(List.of(student));

        mockMvc.perform(get("/api/students/graduated")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].graduated").exists());
    }
}
