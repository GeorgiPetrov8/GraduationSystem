package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.Defense;
import com.example.Graduation_System.data.repo.DefenseRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.DefenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DefenseServiceImpl implements DefenseService {

    @Autowired
    private DefenseRepository defenseRepository;

    @Override
    public List<Defense> getAllDefence() {
        return defenseRepository.findAll();
    }

    @Override
    public Defense getDefenceById(Long id) {
        return defenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Defense with ID " + id + " not found."));
    }

    @Override
    public Defense saveDefence(Defense defense) {
        return defenseRepository.save(defense);
    }

    @Override
    public void deleteDefense(Long id) {
        if (!defenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Defense with ID " + id + " does not exist.");
        }
        defenseRepository.deleteById(id);
    }

    @Override
    public Defense submitGrade(Long defenseId, Double grade) {
        if (grade < 2.0 || grade > 6.0) {
            throw new ValidationException("Grade must be between 2.0 and 6.0");
        }

        Defense defense = defenseRepository.findById(defenseId).orElseThrow(() -> new ResourceNotFoundException("Defense with ID " + defenseId + " not found."));

        defense.setGrades(grade);

        return defenseRepository.save(defense);
    }

    @Override
    public List<Defense> getDefensesWithGradeRange(Double minGrade, Double maxGrade) {
        return defenseRepository.findDefensesWithGradeRange(minGrade, maxGrade);
    }

    @Override
    public Double getAverageStudentsPerDefenseInPeriod(LocalDate startDate, LocalDate endDate) {
        return defenseRepository.findAverageStudentsPerDefenseInPeriod(startDate, endDate);
    }

    @Override
    public Long countSuccessfulDefensesByTeacher(Long teacherId) {
        return defenseRepository.countSuccessfulDefensesByTeacher(teacherId);
    }

    @Override
    public List<Defense>findDefensesByFacultyNumber(String facNumber){
        return defenseRepository.findDefensesByFacultyNumber(facNumber);
    }

    @Override
    public Defense findDefenseById(Long id){
        return defenseRepository.findById(id).orElse(null);
    }
}
