package com.example.Graduation_System.data.repo;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiplomaAssignmentRepository extends JpaRepository<DiplomaAssignment, Long> {
    List<DiplomaAssignment> findByStatus(DiplomaStatus status);

    DiplomaAssignment findByFacultyNumber(String facultyNumber);

    @Query("SELECT da FROM DiplomaAssignment da WHERE da.facultyNumber = :facultyNumber")
    List<DiplomaAssignment> findAllByFacultyNumber(@Param("facultyNumber") String facultyNumber);

    @Query("SELECT da FROM DiplomaAssignment da WHERE da.status = 'APPROVED'")
    List<DiplomaAssignment> findApprovedAssignments();

    @Query("SELECT da FROM DiplomaAssignment da WHERE da.status = 'APPROVED' AND da.supervisor.id = :teacherId")
    List<DiplomaAssignment> findApprovedAssignmentsBySupervisor(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(ne) FROM DiplomaAssignment ne WHERE ne.status = 'REJECTED'")
    Long countNegativeReviews();


}
