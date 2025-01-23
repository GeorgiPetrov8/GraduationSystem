package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.services.DiplomaAssignmentService;
import com.example.Graduation_System.web.api.DiplomaAssignmentApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.anyLong;

class DiplomaAssignmentApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiplomaAssignmentService diplomaAssignmentService;

    @InjectMocks
    private DiplomaAssignmentApiController diplomaAssignmentApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(diplomaAssignmentApiController).build();
    }

    @Test
    void testGetApprovedAssignments() throws Exception {

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setStatus(DiplomaStatus.APPROVED);
        when(diplomaAssignmentService.getApprovedAssignments()).thenReturn(List.of(assignment));

        mockMvc.perform(get("/api/assignments/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(DiplomaStatus.APPROVED.toString()));
    }

    @Test
    void testGetApprovedAssignmentsBySupervisor() throws Exception {

        Long teacherId = 1L;
        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setStatus(DiplomaStatus.APPROVED);
        when(diplomaAssignmentService.getApprovedAssignmentsBySupervisor(anyLong())).thenReturn(List.of(assignment));

        mockMvc.perform(get("/api/assignments/supervisor/{teacherId}", teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(DiplomaStatus.APPROVED.toString()));
    }

    @Test
    void testCountNegativeReviews() throws Exception {

        Long negativeReviewsCount = 5L;
        when(diplomaAssignmentService.countNegativeReviews()).thenReturn(negativeReviewsCount);

        mockMvc.perform(get("/api/assignments/negative/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(negativeReviewsCount));
    }
}
