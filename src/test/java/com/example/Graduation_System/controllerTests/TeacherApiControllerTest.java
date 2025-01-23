package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.*;
import com.example.Graduation_System.services.DiplomaAssignmentService;
import com.example.Graduation_System.services.DiplomaThesisService;
import com.example.Graduation_System.services.ReviewService;
import com.example.Graduation_System.services.StudentService;
import com.example.Graduation_System.services.TeacherService;
import com.example.Graduation_System.cfg.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TeacherApiControllerTest {

    @Mock
    private DiplomaAssignmentService diplomaAssignmentService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @Mock
    private JwtService jwtService;

    @Mock
    private DiplomaThesisService diplomaThesisService;

    private MockMvc mockMvc;
    private TeacherApiController teacherApiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        teacherApiController = new TeacherApiController();
        teacherApiController.diplomaAssignmentService = diplomaAssignmentService;
        teacherApiController.reviewService = reviewService;
        teacherApiController.teacherService = teacherService;
        teacherApiController.studentService = studentService;
        teacherApiController.jwtService = jwtService;
        teacherApiController.diplomaThesisService = diplomaThesisService;

        Authentication authentication = new UsernamePasswordAuthenticationToken("teacher1", null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc = MockMvcBuilders.standaloneSetup(teacherApiController).build();
    }

    @Test
    public void testCreateAssignment() throws Exception {

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setFacultyNumber("12345");
        Teacher teacher = new Teacher();
        teacher.setName("teacher1");
        when(teacherService.getTeacherByUserUsername("teacher1")).thenReturn(teacher);
        when(studentService.findByFacultyNumber("12345")).thenReturn(new Student());
        when(diplomaAssignmentService.saveAssignment(any(DiplomaAssignment.class))).thenReturn(assignment);

        mockMvc.perform(post("/api/teacher/assignments")
                        .contentType("application/json")
                        .content("{\"facultyNumber\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyNumber").value("12345"));
    }

    @Test
    public void testSubmitReview() throws Exception {

        Review review = new Review();
        review.setFacultyNumber("12345");
        Teacher teacher = new Teacher();
        teacher.setName("teacher1");
        DiplomaThesis diplomaThesis = new DiplomaThesis();
        diplomaThesis.setFacultyNumber("12345");
        when(teacherService.getTeacherByUserUsername("teacher1")).thenReturn(teacher);
        when(diplomaThesisService.findDiplomaThesisByFacultyNumber("12345")).thenReturn(diplomaThesis);
        when(reviewService.saveReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/teacher/reviews")
                        .contentType("application/json")
                        .content("{\"facultyNumber\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facultyNumber").value("12345"));
    }

    @Test
    public void testGetLoggedInTeacherWithValidToken() throws Exception {

        String validToken = "ValidToken";
        Teacher teacher = new Teacher();
        teacher.setName("teacher1");
        when(jwtService.extractUsername(validToken)).thenReturn("teacher1");
        when(teacherService.getTeacherByUserUsername("teacher1")).thenReturn(teacher);

        mockMvc.perform(get("/api/teacher/me")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));  // Print full response body
    }
}
