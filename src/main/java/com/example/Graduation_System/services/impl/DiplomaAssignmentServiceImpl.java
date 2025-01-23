package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.repo.DiplomaAssignmentRepository;
import com.example.Graduation_System.services.DiplomaAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiplomaAssignmentServiceImpl implements DiplomaAssignmentService {

    @Autowired
    private DiplomaAssignmentRepository diplomaAssignmentRepository;

    @Override
    public List<DiplomaAssignment> getAllAssignments() {
        return diplomaAssignmentRepository.findAll();
    }

    @Override
    public DiplomaAssignment getAssignmentById(Long id) {
        return diplomaAssignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Diploma Assignment not found"));
    }

    @Override
    public DiplomaAssignment saveAssignment(DiplomaAssignment diplomaAssignment) {
        return diplomaAssignmentRepository.save(diplomaAssignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        diplomaAssignmentRepository.deleteById(id);
    }

    @Override
    public List<DiplomaAssignment> getApprovedAssignments() {
        return diplomaAssignmentRepository.findApprovedAssignments();
    }

    @Override
    public void approveAssignment(Long id) {
        DiplomaAssignment assignment = diplomaAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(DiplomaStatus.APPROVED);

        diplomaAssignmentRepository.save(assignment);
    }

    @Override
    public void disapproveAssignment(Long id) {
        DiplomaAssignment assignment = diplomaAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(DiplomaStatus.REJECTED);

        diplomaAssignmentRepository.save(assignment);
    }

    @Override
    public List<DiplomaAssignment> getApprovedAssignmentsBySupervisor(Long teacherId) {
        return diplomaAssignmentRepository.findApprovedAssignmentsBySupervisor(teacherId);
    }

    @Override
    public Long countNegativeReviews() {
        return diplomaAssignmentRepository.countNegativeReviews();
    }

    @Override
    public DiplomaAssignment findAssignmentByNumber(String facultyNumber) {
        return diplomaAssignmentRepository.findByFacultyNumber(facultyNumber);
    }

    @Override
    public List<DiplomaAssignment> findAllByFacultyNumber(String facultyNumber) {
        return diplomaAssignmentRepository.findAllByFacultyNumber(facultyNumber);
    }
}
