package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.Defense;
import com.example.Graduation_System.services.DefenseService;
import com.example.Graduation_System.web.api.DefenseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DefenseApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DefenseService defenseService;

    @InjectMocks
    private DefenseApiController defenseApiController;

    private Defense defense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(defenseApiController).build();

        defense = new Defense();
        defense.setId(1L);
        defense.setGrades(3.5);
    }

    @Test
    void testGetDefensesByGradeRange() throws Exception {

        List<Defense> defenses = List.of(defense);
        when(defenseService.getDefensesWithGradeRange(2.0, 5.0)).thenReturn(defenses);

        mockMvc.perform(get("/api/defense/grades")
                        .param("minGrade", "2.0")
                        .param("maxGrade", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grades").value(3.5));
    }

    @Test
    void testGetAverageStudentsPerDefense() throws Exception {

        when(defenseService.getAverageStudentsPerDefenseInPeriod(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)))
                .thenReturn(5.0);

        mockMvc.perform(get("/api/defense/average-students")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5.0));
    }

    @Test
    void testCountSuccessfulDefensesByTeacher() throws Exception {

        when(defenseService.countSuccessfulDefensesByTeacher(1L)).thenReturn(3L);

        mockMvc.perform(get("/api/defense/successful/{teacherId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }
}
