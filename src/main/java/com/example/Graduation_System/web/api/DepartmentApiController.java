package com.example.Graduation_System.web.api;

import com.example.Graduation_System.cfg.JwtService;
import com.example.Graduation_System.data.*;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.services.*;
import com.example.Graduation_System.services.impl.DepartmentServiceImpl;
import com.example.Graduation_System.services.impl.DiplomaThesisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/department")
@PreAuthorize("hasRole('DEP')")
public class DepartmentApiController {
    @Autowired
    private DiplomaAssignmentService diplomaAssignmentService;

    @Autowired
    private DefenseService defenseService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/assignments/number")
    public List<DiplomaAssignment> findAllAssignments(@RequestParam String facNumber) {
        return diplomaAssignmentService.findAllByFacultyNumber(facNumber);
    }

    @PostMapping("/assignments/approve/{id}")
    public String approveAssignment(@PathVariable Long id) {
        diplomaAssignmentService.approveAssignment(id);
        return "Assignment with ID " + id + " has been approved.";
    }

    @PostMapping("/assignments/disapprove/{id}")
    public String disapproveAssignment(@PathVariable Long id) {
        diplomaAssignmentService.disapproveAssignment(id);
        return "Assignment with ID " + id + " has not been approved.";
    }

    @GetMapping("/defense/number")
    public List<Defense> findAllDefenses(@RequestParam String facNumber) {
        return defenseService.findDefensesByFacultyNumber(facNumber);
    }

    @PostMapping("/defense/{id}/grade")
    public void assignGrades(@PathVariable Long id, @RequestBody Double grades) {
        if (grades > 2) {
            Defense defense = defenseService.findDefenseById(id);
            Student student = studentService.findByFacultyNumber(defense.getFacultyNumber());

            student.setGraduated(LocalDate.now());

            studentService.saveStudent(student);
        }
        defenseService.submitGrade(id, grades);
    }
}
