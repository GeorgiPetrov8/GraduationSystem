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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherApiController {
    @Autowired
    protected DiplomaAssignmentService diplomaAssignmentService;

    @Autowired
    protected DefenseService defenseService;

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected TeacherService teacherService;

    @Autowired
    protected StudentService studentService;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected DepartmentService departmentService;

    @Autowired
    protected DiplomaThesisService diplomaThesisService;

    @PostMapping("/assignments")
    public DiplomaAssignment createAssignment(@RequestBody DiplomaAssignment assignment) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Teacher teacher = teacherService.getTeacherByUserUsername(username);

        String facultyNumber = assignment.getFacultyNumber();
        Student student = studentService.findByFacultyNumber(facultyNumber);

        assignment.setStudent(student);
        assignment.setSupervisor(teacher);
        assignment.setStatus(DiplomaStatus.PENDING);
        return diplomaAssignmentService.saveAssignment(assignment);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInTeacher(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        // Extract the token by removing the "Bearer " prefix
        String token = authorizationHeader.substring(7);

        String username = jwtService.extractUsername(token);
        Teacher teacher = teacherService.getTeacherByUserUsername(username);
        if (teacher == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No associated teacher found for the user.");
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/reviews")
    public Review submitReview(@RequestBody Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Teacher teacher = teacherService.getTeacherByUserUsername(username);

        String facultyNumber = review.getFacultyNumber();

        DiplomaThesis diplomaThesis = diplomaThesisService.findDiplomaThesisByFacultyNumber(facultyNumber);

        review.setThesis(diplomaThesis);
        review.setReviewer(teacher);
        review.setReviewDate(new Date());
        return reviewService.saveReview(review);
    }


    @PostMapping("/defenses")
    public Defense scheduleDefense(@RequestBody Defense defense) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Teacher teacher = teacherService.getTeacherByUserUsername(username);

        String depName = defense.getDepartmentName();
        Department department = departmentService.findDepartmentByName(depName);

        String facultyNumber = defense.getFacultyNumber();
        Student student = studentService.findByFacultyNumber(facultyNumber);

        defense.setSupervisor(teacher);
        defense.setStudents(student);
        defense.setCommittee(department);
        return defenseService.saveDefence(defense);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/assignments")
    public DiplomaAssignment getAssignmentByFacultyNumber(@RequestParam("facultyNumber") String facultyNumber) {
        return diplomaAssignmentService.findAssignmentByNumber(facultyNumber);
    }
}
