package com.example.Graduation_System.services;

import com.example.Graduation_System.data.Defense;

import java.time.LocalDate;
import java.util.List;

public interface DefenseService {
    List<Defense> getAllDefence();

    Defense getDefenceById(Long id);

    Defense saveDefence(Defense defense);

    void deleteDefense(Long id);

    Defense submitGrade(Long defenseId, Double grade);

    List<Defense> getDefensesWithGradeRange(Double minGrade, Double maxGrade);

    Double getAverageStudentsPerDefenseInPeriod(LocalDate startDate, LocalDate endDate);

    Long countSuccessfulDefensesByTeacher(Long teacherId);

    List<Defense>findDefensesByFacultyNumber(String facNumber);

    Defense findDefenseById(Long id);
}
