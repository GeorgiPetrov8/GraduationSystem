package com.example.Graduation_System.services;

import com.example.Graduation_System.data.DiplomaAssignment;

import java.util.List;

public interface DiplomaAssignmentService {

    List<DiplomaAssignment> getAllAssignments();

    DiplomaAssignment getAssignmentById(Long id);

    DiplomaAssignment saveAssignment(DiplomaAssignment diplomaAssignment);

    void deleteAssignment(Long id);

    List<DiplomaAssignment> getApprovedAssignments();

    void approveAssignment(Long id);

    void disapproveAssignment(Long id);

    List<DiplomaAssignment> getApprovedAssignmentsBySupervisor(Long teacherId);

    Long countNegativeReviews();

    DiplomaAssignment findAssignmentByNumber(String facultyNumber);

    List<DiplomaAssignment> findAllByFacultyNumber(String facultyNumber);
}
