package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.Defense;
import com.example.Graduation_System.data.repo.DefenseRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.impl.DefenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefenseServiceImplTest {

    @Mock
    private DefenseRepository defenseRepository;

    @InjectMocks
    private DefenseServiceImpl defenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDefense() {

        List<Defense> defenses = Arrays.asList(new Defense(), new Defense());
        when(defenseRepository.findAll()).thenReturn(defenses);

        List<Defense> result = defenseService.getAllDefence();

        assertEquals(defenses.size(), result.size());
        verify(defenseRepository, times(1)).findAll();
    }

    @Test
    void testGetDefenseById_Found() {

        Long defenseId = 1L;
        Defense defense = new Defense();
        when(defenseRepository.findById(defenseId)).thenReturn(Optional.of(defense));

        Defense result = defenseService.getDefenceById(defenseId);

        assertNotNull(result);
        verify(defenseRepository, times(1)).findById(defenseId);
    }

    @Test
    void testGetDefenseById_NotFound() {

        Long defenseId = 1L;
        when(defenseRepository.findById(defenseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> defenseService.getDefenceById(defenseId));
        verify(defenseRepository, times(1)).findById(defenseId);
    }

    @Test
    void testSaveDefense() {

        Defense defense = new Defense();
        when(defenseRepository.save(defense)).thenReturn(defense);

        Defense result = defenseService.saveDefence(defense);

        assertNotNull(result);
        verify(defenseRepository, times(1)).save(defense);
    }

    @Test
    void testDeleteDefense_Success() {

        Long defenseId = 1L;
        when(defenseRepository.existsById(defenseId)).thenReturn(true);

        defenseService.deleteDefense(defenseId);

        verify(defenseRepository, times(1)).existsById(defenseId);
        verify(defenseRepository, times(1)).deleteById(defenseId);
    }

    @Test
    void testDeleteDefense_NotFound() {

        Long defenseId = 1L;
        when(defenseRepository.existsById(defenseId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> defenseService.deleteDefense(defenseId));
        verify(defenseRepository, times(1)).existsById(defenseId);
    }

    @Test
    void testSubmitGrade_ValidGrade() {

        Long defenseId = 1L;
        Double grade = 5.0;
        Defense defense = new Defense();
        when(defenseRepository.findById(defenseId)).thenReturn(Optional.of(defense));
        when(defenseRepository.save(defense)).thenReturn(defense);

        Defense result = defenseService.submitGrade(defenseId, grade);

        assertNotNull(result);
        assertEquals(grade, result.getGrades());
        verify(defenseRepository, times(1)).findById(defenseId);
        verify(defenseRepository, times(1)).save(defense);
    }

    @Test
    void testSubmitGrade_InvalidGrade() {

        Long defenseId = 1L;
        Double grade = 1.0;

        assertThrows(ValidationException.class, () -> defenseService.submitGrade(defenseId, grade));
        verify(defenseRepository, never()).findById(defenseId);
    }

    @Test
    void testGetDefensesWithGradeRange() {

        Double minGrade = 3.0;
        Double maxGrade = 6.0;
        List<Defense> defenses = Arrays.asList(new Defense(), new Defense());
        when(defenseRepository.findDefensesWithGradeRange(minGrade, maxGrade)).thenReturn(defenses);


        List<Defense> result = defenseService.getDefensesWithGradeRange(minGrade, maxGrade);

        assertEquals(defenses.size(), result.size());
        verify(defenseRepository, times(1)).findDefensesWithGradeRange(minGrade, maxGrade);
    }

    @Test
    void testGetAverageStudentsPerDefenseInPeriod() {

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Double average = 2.5;
        when(defenseRepository.findAverageStudentsPerDefenseInPeriod(startDate, endDate)).thenReturn(average);

        Double result = defenseService.getAverageStudentsPerDefenseInPeriod(startDate, endDate);

        assertEquals(average, result);
        verify(defenseRepository, times(1)).findAverageStudentsPerDefenseInPeriod(startDate, endDate);
    }
}
