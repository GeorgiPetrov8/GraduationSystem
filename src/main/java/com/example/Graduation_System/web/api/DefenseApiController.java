package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.Defense;
import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.services.DefenseService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/defense")
public class DefenseApiController {
    @Autowired
    private DefenseService defenseService;

    @GetMapping("/grades")
    public List<Defense> getDefensesByGradeRange(@RequestParam Double minGrade, @RequestParam Double maxGrade) {
        return defenseService.getDefensesWithGradeRange(minGrade, maxGrade);
    }

    @GetMapping("/average-students")
    public Double getAverageStudentsPerDefense(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return defenseService.getAverageStudentsPerDefenseInPeriod(startDate, endDate);
    }

    @GetMapping("/successful/{teacherId}")
    public Long countSuccessfulDefensesByTeacher(@PathVariable Long teacherId) {
        return defenseService.countSuccessfulDefensesByTeacher(teacherId);
    }
}
