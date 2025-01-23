package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.services.DiplomaAssignmentService;
import com.example.Graduation_System.services.StudentService;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class DiplomaAssignmentApiController {
    @Autowired
    private DiplomaAssignmentService diplomaAssignmentService;

    @GetMapping("/approved")
    public List<DiplomaAssignment> getApprovedAssignments() {
        return diplomaAssignmentService.getApprovedAssignments();
    }

    @GetMapping("/supervisor/{teacherId}")
    public List<DiplomaAssignment> getApprovedAssignmentsBySupervisor(@PathVariable Long teacherId) {
        return diplomaAssignmentService.getApprovedAssignmentsBySupervisor(teacherId);
    }

    @GetMapping("/negative/count")
    public Long countNegativeReviews() {
        return diplomaAssignmentService.countNegativeReviews();
    }
}