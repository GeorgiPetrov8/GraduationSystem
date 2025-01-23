package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.Department;
import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.DiplomaThesis;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.services.DiplomaAssignmentService;
import com.example.Graduation_System.services.DiplomaThesisService;
import com.example.Graduation_System.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentApiController {
    @Autowired
    private DiplomaAssignmentService diplomaAssignmentService;

    @Autowired
    private DiplomaThesisService diplomaThesisService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/assignments/approved")
    public List<DiplomaAssignment> viewApprovedAssignments() {
        return diplomaAssignmentService.getApprovedAssignments();
    }

    @PostMapping("/thesis")
    public DiplomaThesis submitThesis(@RequestBody DiplomaThesis thesis) {
        String facNum = thesis.getFacultyNumber();
        DiplomaAssignment assignment = diplomaAssignmentService.findAssignmentByNumber(facNum);
        if (facNum == null || facNum.isEmpty()) {
            throw new IllegalArgumentException("Faculty number is required.");
        }
        thesis.setAssignment(assignment);
        thesis.setSubmissionDate(new Date());
        return diplomaThesisService.saveThesis(thesis);
    }

    @GetMapping("/graduated")
    public List<Student> getGraduatedStudents(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return studentService.getGraduatedStudentsInPeriod(startDate, endDate);
    }

    @GetMapping("/assignments/my")
    public List<DiplomaAssignment> viewMyAssignments(@RequestParam String facNumber) {
        return diplomaAssignmentService.findAllByFacultyNumber(facNumber);
    }
}
